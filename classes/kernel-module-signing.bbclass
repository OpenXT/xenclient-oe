require classes/module-signing.bbclass
# override value from module-signing to use the in-tree utility
SIGN_FILE = "${B}/scripts/sign-file"

# Set KERNEL_MODULE_SIG_CERT in local.conf to the filepath of a public key
# to embed in the kernel to verify signed modules
export KERNEL_MODULE_SIG_CERT

do_configure_append() {
    if [ -n "${KERNEL_MODULE_SIG_CERT}" ] &&
       grep -q '^CONFIG_MODULE_SIG=y' ${B}/.config ; then
        sed -i -e '/CONFIG_MODULE_SIG_KEY[ =]/d' ${B}/.config
        echo "CONFIG_MODULE_SIG_KEY=\"${KERNEL_MODULE_SIG_CERT}\"" >> \
               ${B}/.config
        sed -i -e '/CONFIG_MODULE_SIG_ALL[ =]/d' ${B}/.config
        echo "# CONFIG_MODULE_SIG_ALL is not set" >> \
               ${B}/.config
    fi
}

def get_signing_key(d):
    path = d.getVar("KERNEL_MODULE_SIG_CERT") or os.path.join(d.getVar("STAGING_KERNEL_BUILDDIR"),"certs","signing_key.x509")
    return path + ":" + str(os.path.exists(path))

do_shared_workdir[file-checksums] = "${@get_signing_key(d)}"
