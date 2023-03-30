DESCRIPTION = "Basic grub.cfg for PVH/PV domains"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
"

APPEND ??= "root=/dev/xvda1 ro console=hvc0"
# Make sure the package is machine specific since it uses APPEND
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d "${D}/boot/grub2"

    echo "default=boot" > "${D}/boot/grub2/grub.cfg"
    echo "timeout=0" >> "${D}/boot/grub2/grub.cfg"
    echo "menuentry 'boot' {" >> "${D}/boot/grub2/grub.cfg"
    echo "linux /boot/${KERNEL_IMAGETYPE} ${APPEND}" >> "${D}/boot/grub2/grub.cfg"
    echo "}" >> "${D}/boot/grub2/grub.cfg"
}

FILES_${PN} = "/boot"
