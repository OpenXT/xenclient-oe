# For module signing
DEPENDS += "openssl-native"

# Let linux build strip modules before signing
export INSTALL_MOD_STRIP="1"
# Don't let OE strip packages since that will remove the signature
INHIBIT_PACKAGE_STRIP = "1"

# Needed to build sign-tool
export HOST_EXTRACFLAGS = "${BUILD_CFLAGS} ${BUILD_LDFLAGS}"

# Set KERNEL_MODULE_SIG_KEY in local.conf to the filepath of a private key
# for signing kernel modules. If unset, signing can be done offline, but
# KERNEL_MODULE_OFFLINE_SIGNING must be set to "1".
export KERNEL_MODULE_SIG_KEY
export KERNEL_MODULE_OFFLINE_SIGNING ?= "0"
# Set KERNEL_MODULE_SIG_CERT in local.conf to the filepath of the corresponging
# public key to verify the signed modules.
export KERNEL_MODULE_SIG_CERT

def get_signing_cert(d):
    path = d.getVar("KERNEL_MODULE_SIG_CERT")
    if path:
        return path + ":" + str(os.path.exists(path))
    return ""

def get_signing_key(d):
    path = d.getVar("KERNEL_MODULE_SIG_KEY")
    if path:
        return path + ":" + str(os.path.exists(path))
    return ""

# Kernel builds will override this with ${B}/scripts/sign-file
SIGN_FILE = "${STAGING_KERNEL_BUILDDIR}/scripts/sign-file"

fakeroot do_sign_modules() {
    if ! grep -q '^CONFIG_MODULE_SIG=y' "${STAGING_KERNEL_BUILDDIR}/.config"; then
        bbnote "Kernel module signing deactivated in kernel configuration ${STAGING_KERNEL_BUILDDIR}/.config."
        return
    fi
    if [ -z "${KERNEL_MODULE_SIG_CERT}" ]; then
        bbfatal "Kernel module signing should only be used when setting \
KERNEL_MODULE_SIG_CERT in local.conf."
    fi

    if [ -n "${KERNEL_MODULE_SIG_KEY}" ] &&
       [ "${KERNEL_MODULE_OFFLINE_SIGNING}" = "1" ] ; then
       bbfatal "Setting KERNEL_MODULE_SIG_KEY and KERNEL_MODULE_OFFLINE_SIGNING is inconsistent.  Choose one or the other."
    fi

    if [ "${KERNEL_MODULE_OFFLINE_SIGNING}" = "1" ]; then
        bbnote "Skipping do_sign_modules - Offline signing required for usable image"
        return
    fi

    if [ -z "${KERNEL_MODULE_SIG_KEY}" ] &&
       [ "${KERNEL_MODULE_OFFLINE_SIGNING}" != "1" ] ; then
        bbfatal "
No KERNEL_MODULE_SIG_KEY provided, and offline kernel module signing has not
been enabled by setting KERNEL_MODULE_OFFLINE_SIGNING = \"1\".  Set
KERNEL_MODULE_OFFLINE_SIGNING if you want to continue.  The bitbake output
image will not boot properly unless the .ko kernel modules are manually signed.
"
    else
        SIG_HASH=$( grep CONFIG_MODULE_SIG_HASH= \
                        ${STAGING_KERNEL_BUILDDIR}/.config | \
                      cut -d '"' -f 2 )
        [ -z "$SIG_HASH" ] && bbfatal "CONFIG_MODULE_SIG_HASH is not set in .config"

        [ -x "${SIGN_FILE}" ] || bbfatal "Cannot find scripts/sign-file"

        find ${D} -name "*.ko" -print0 | \
          xargs -t --no-run-if-empty -0 -n 1 \
              ${SIGN_FILE} $SIG_HASH ${KERNEL_MODULE_SIG_KEY} \
                  ${KERNEL_MODULE_SIG_CERT}
    fi
}

addtask sign_modules after do_install before do_package
# lockfiles needs to match module.bbclass make_scripts value
# Otherwise sign-file could disappear from ${STAGING_KERNEL_BUILDDIR}
# Build-time auto-generated keys sign modules in do_install
do_install[lockfiles] = "${TMPDIR}/kernel-scripts.lock"
# Explicit keys sign modules in do_sign_modules
do_sign_modules[lockfiles] = "${TMPDIR}/kernel-scripts.lock"

do_sign_modules[depends] += "virtual/kernel:do_shared_workdir"
do_sign_modules[file-checksums] += "${@get_signing_key(d)} ${@get_signing_cert(d)}"
