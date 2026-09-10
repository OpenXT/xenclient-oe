require recipes-kernel/linux/linux-openxt-copy-objtool.inc
require classes/kernel-module-signing.bbclass

# xenclient-syncui is a dojosdk version override only and has no defconfig.
COMPATIBLE_MACHINE = "xenclient-dom0|xenclient-ndvm|xenclient-uivm|usbvm|openxt-installer|xenclient-stubdomain|xenclient-syncvm"

DEPENDS += "bc-native"

S = "${UNPACKDIR}/linux-${PV}"

# meta-oe linux.inc still reads ${WORKDIR}/defconfig. file:// unpacks to
# UNPACKDIR on Wrynose, so stage it where that prepend expects it.
do_configure:prepend() {
    if [ ! -e ${WORKDIR}/defconfig ] && [ -e ${UNPACKDIR}/defconfig ]; then
        cp ${UNPACKDIR}/defconfig ${WORKDIR}/defconfig
    fi
}

# Don't use update-alternatives for kernel at all. 
pkg_postinst:kernel-image () {
    mv $D/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} $D/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
}

pkg_postrm:kernel-image () {
    rm -f $D/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
}
