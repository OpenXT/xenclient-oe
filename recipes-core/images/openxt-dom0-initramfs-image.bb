SUMMARY = "Initramfs image for OpenXT dom0"
DESCRIPTION = "Initramfs image containing the early boot components required to \
bootsrap OpenXT dom0."
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"
COMPATIBLE_MACHINE = "(openxt-dom0)"

IMAGE_FSTYPES = "cpio.gz"
IMAGE_INSTALL = " \
    busybox \
    initramfs-module-functions \
    initramfs-module-lvm \
    initramfs-module-udev \
    initramfs-module-bootfs \
    initramfs-module-tpm \
    initramfs-module-tpm2 \
    initramfs-module-selinux \
"
IMAGE_LINGUAS = "en-us"

inherit image

NO_RECOMMENDATIONS = "1"
BAD_RECOMMENDATIONS += " \
    ldconfig \
    busybox-syslog \
    busybox-udhcpc \
"

PACKAGE_REMOVE = " \
    kernel-image-* \
"
post_rootfs_shell_commands() {
    rm -f ${IMAGE_ROOTFS}/sbin/udhcpc;
    rm -rvf ${IMAGE_ROOTFS}/usr/lib/opkg;
}
ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_shell_commands; "
