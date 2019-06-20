require recipes-extended/xen/xen.inc
require xen-common.inc

DESCRIPTION = "Xen hypervisor, 64-bit build"

# In OpenXT, multiple recipes are used to build Xen and its components:
# a 32-bit build of tools ; a 64-bit hypervisor; and a separate blktap
# build to fix potentially circular dependencies with libargo and icbinn.
#
# This recipe shares a common xen.inc with other recipes.
# PN in this recipe is "xen-hypervisor", rather than "xen" as xen.inc is
# written to expect, so in order to produce the expected package names
# with a "xen-" rather than "xen-hypervisor-" prefix, this python section
# renames the FILES_... variables defined in xen.inc.
# Some package names are defined explicitly rather than using ${PN}.

python () {
    for PKG in ['hypervisor']:
        d.renameVar("FILES_xen-hypervisor-" + PKG, "FILES_xen-" + PKG)
}

PROVIDES = "xen-hypervisor"

PACKAGES = " \
    ${PN}-dbg \
    xen-efi \
    xen-hypervisor \
    "

FILES_xen-efi = "\
    /boot/xen.efi \
    "

PROVIDES_xen-efi = "xen-efi"
PROVIDES_xen-hypervisor = "xen-hypervisor"

INSANE_SKIP_${PN}-dbg = "arch"

INITSCRIPT_PACKAGES = ""
SYSTEMD_PACKAGES = ""

# Undo some of the upstream xen.inc configuration to retain
# the classic OpenXT Xen build configuration:

EXTRA_OECONF_remove = " \
    --exec-prefix=/usr \
    --prefix=/usr \
    --host=${HOST_SYS} \
    --with-systemd=${systemd_unitdir}/system \
    --with-systemd-modules-load=${systemd_unitdir}/modules-load.d \
    --disable-stubdom \
    --disable-ioemu-stubdom \
    --disable-pv-grub \
    --disable-xenstore-stubdom \
    --disable-rombios \
    --disable-ocamltools \
    --with-initddir=${INIT_D_DIR} \
    --with-sysconfig-leaf-dir=default \
    --with-system-qemu=/usr/bin/qemu-system-i386 \
    --disable-qemu-traditional \
"

EXTRA_OEMAKE += " \
    XEN_TARGET_ARCH=x86_64 \
    XEN_VENDORVERSION=-xc \
    "

do_configure() {

    echo "debug := n" > .config
    echo "XSM_ENABLE := y" >> .config
    echo "FLASK_ENABLE := y" >> .config

    cp "${WORKDIR}/defconfig" "${B}/xen/.config"
    #Define CONFIG_TXT_OP in the hypervisor build to export tboot evtlog data
    #It's stubbed out for the pv-shim since it's not supported, but uses the
    #same hypercall headers
    echo "CONFIG_TXT_OP=y" >> "${B}/xen/.config"

    # Enable argo
    echo "CONFIG_ARGO=y" >> "${B}/xen/.config"

    # do configure
    oe_runconf
}

do_compile() {
    unset CFLAGS
    export CC="${HOST_PREFIX}gcc ${TOOLCHAIN_OPTIONS}"
    export CPP="${HOST_PREFIX}cpp ${TOOLCHAIN_OPTIONS}"

    oe_runmake -C xen olddefconfig
}

do_install() {
    unset CFLAGS
    export CC="${HOST_PREFIX}gcc ${TOOLCHAIN_OPTIONS}"
    export CPP="${HOST_PREFIX}cpp ${TOOLCHAIN_OPTIONS}"
    install -d ${D}/boot
    oe_runmake DESTDIR=${D} install-xen
    ln -sf "`basename ${D}/boot/xen-*xc.gz`" ${D}/boot/xen-debug.gz
    install -m 600 ${B}/xen/xen.efi ${D}/boot/

    rm -rf ${D}/usr/lib64
}

RPROVIDES_xen-efi = "xen-efi"
RPROVIDES_xen-hypervisor = "xen-hypervisor"
