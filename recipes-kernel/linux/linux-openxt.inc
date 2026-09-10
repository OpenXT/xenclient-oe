require recipes-kernel/linux/linux-openxt-copy-objtool.inc
require classes/kernel-module-signing.bbclass

DEPENDS += "bc-native"

S = "${UNPACKDIR}/linux-${PV}"

# Don't use update-alternatives for kernel at all. 
pkg_postinst:kernel-image () {
    mv $D/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} $D/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
}

pkg_postrm:kernel-image () {
    rm -f $D/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
}
