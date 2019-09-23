# XenClient dom0 test image.

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

inherit openxt-selinux-image
inherit openxt-vm-common

INITRD_VM = "xenclient-initramfs-image"
INSTALL_VM_INITRD = "1"

IMAGE_FEATURES += " \
    package-management \
    read-only-rootfs \
    root-bash-shell \
    wildcard-sshd-argo \
    bats \
"
IMAGE_FSTYPES = "ext3.gz"
export IMAGE_BASENAME = "xenclient-dom0-test-image"

COMPATIBLE_MACHINE = "(xenclient-dom0)"

require xenclient-dom0-image.inc

IMAGE_INSTALL += " \
    packagegroup-openxt-test \
"
