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
    tpm-tools-sa \
    tpm2-tss \
    tpm2-tools \
    initramfs-module-lvm \
    initramfs-module-bootfs \
    initramfs-module-tpm \
    initramfs-module-tpm2 \
    initramfs-module-selinux \
    xenclient-initramfs-shared-libs \
    kernel-module-tpm \
    kernel-module-tpm-tis \
    kernel-module-tpm-tis-core \
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

inherit image

write_initramfs_config_files() {
    install -m 0644 ${WORKDIR}/initramfs-lvm.conf ${IMAGE_ROOTFS}/${sysconfdir}/lvm/lvm.conf
}
ROOTFS_POSTPROCESS_COMMAND += " write_initramfs_config_files; "

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
