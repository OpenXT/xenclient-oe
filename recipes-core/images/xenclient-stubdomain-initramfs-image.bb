# Stubdomain initramfs image.

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

IMAGE_FSTYPES = "cpio.gz"

COMPATIBLE_MACHINE = "(xenclient-stubdomain)"

IMAGE_INSTALL = " \
    busybox \
    bridge-utils \
    initramfs-stubdomain \
    xen-xenstore \
    qemu-dm-stubdom \
    argo-module \
    simple-poweroff \
    rsyslog \
"
IMAGE_LINGUAS = ""

# List of packages removed at rootfs-postprocess.
# - Remove any kernel-image that the kernel-module-* packages may have pulled in.
# - Remove udev (use busybox-mdev instead, this is a simple initramfs).
# - Remove sysvinit (no need for init management).
PACKAGE_REMOVE = " \
    kernel-image-* \
    udev \
    sysvinit \
"

inherit image

post_rootfs_shell_commands() {
    opkg -f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} ${OPKG_ARGS} -force-depends remove ${PACKAGE_REMOVE};

    rm -f ${IMAGE_ROOTFS}/sbin/udhcpc;
    rm -f ${IMAGE_ROOTFS}/sbin/ldconfig;
    rm -rvf ${IMAGE_ROOTFS}/usr/lib/opkg;
}
ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_shell_commands; "
