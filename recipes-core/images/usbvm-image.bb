DESCRIPTION = "usbvm to isolate USB hardware"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
"

inherit openxt-selinux-image

IMAGE_FEATURES += " \
    read-only-rootfs \
    empty-root-password \
    ctrlaltdel-reboot \
"

IMAGE_FSTYPES = "ext4.disk.vhd.gz"

IMAGE_LINGUAS = ""

COMPATIBLE_MACHINE = "usbvm"

IMAGE_INSTALL += " \
    packagegroup-core-boot \
    kmod \
    openssh \
    rsyslog \
    usbutils \
    grub-xen-conf \
    kernel-modules \
    vusb-daemon-stub \
    argo-input-sender \
"
