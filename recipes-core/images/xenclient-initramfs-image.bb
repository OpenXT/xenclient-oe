SUMMARY = "Initramfs image for OpenXT dom0"
DESCRIPTION = "This image provide the tools required early on to boot OpenXT's dom0."
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"
SRC_URI = " \
    file://initramfs-lvm.conf \
"
COMPATIBLE_MACHINE = "(xenclient-dom0)"

IMAGE_FSTYPES = "cpio.gz"
IMAGE_INSTALL = " \
    busybox \
    lvm2 \
    libtss2 \
    libtctisocket \
    libtctidevice \
    tpm-tools-sa \
    tpm2-tools \
    initramfs-xenclient \
    xenclient-initramfs-shared-libs \
    kernel-module-tpm \
    kernel-module-tpm-tis \
    kernel-module-tpm-tis-core \
    kernel-module-fbcon \
    kernel-module-tileblit \
    kernel-module-font \
    kernel-module-bitblit \
    kernel-module-softcursor \
    kernel-module-usbhid \
    kernel-module-ehci-hcd \
    kernel-module-ehci-pci \
    kernel-module-uhci-hcd \
    kernel-module-ohci-hcd \
    kernel-module-hid \
    kernel-module-hid-generic \
    module-init-tools-depmod \
    module-init-tools \
    policycoreutils-setfiles \
"
IMAGE_LINGUAS = "en-us"
# udev is widely depended upon... odds are it will still be around.
IMAGE_DEV_MANAGER = "busybox-mdev"
IMAGE_BOOT = "${IMAGE_DEV_MANAGER}"

write_initramfs_config_files() {
    install -m 0644 ${WORKDIR}/initramfs-lvm.conf ${IMAGE_ROOTFS}/${sysconfdir}/lvm/lvm.conf
}
ROOTFS_POSTPROCESS_COMMAND += " \
    write_initramfs_config_files; \
"

inherit image

# Re-enable do_fetch/do_unpack to fetch image specific configuration files
# (see SRC_URI).
# This could otherwise be achieved at the recipe level by packaging a specific
# configuration package:
# - lvm2 (lvm.conf)
addtask rootfs after do_unpack
python () {
    d.delVarFlag("do_fetch", "noexec")
    d.delVarFlag("do_unpack", "noexec")
}
