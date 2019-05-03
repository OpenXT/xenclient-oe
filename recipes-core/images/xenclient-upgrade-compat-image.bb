SUMMARY = "Upgrade compat image for pre stable-9 upgrades"
DESCRIPTION = "This image provides minimal tools to support pre stable-9 upgrades."
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"
COMPATIBLE_MACHINE = "(upgrade-compat)"

IMAGE_FSTYPES = "ext3.gz"
export IMAGE_BASENAME = "xenclient-upgrade-compat-image"
IMAGE_INSTALL = " \
    busybox \
    coreutils \
    lvm2 \
    grub \
    policycoreutils \
    policycoreutils-setfiles \
    pesign \
    xenclient-tpm-scripts \
    tpm-tools-sa \
    tpm2-tss \
    tpm2-tools \
    openxt-measuredlaunch \
    openxt-keymanagement \
    xenclient-dom0-tweaks \
    xenclient-splash-images \
"
IMAGE_LINGUAS = "en-us"

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

create_data_dir() {
   mkdir -p ${IMAGE_ROOTFS}/config
   mkdir -p ${IMAGE_ROOTFS}/etc/xen/xenrefpolicy/policy
   mkdir -p ${IMAGE_ROOTFS}/etc/selinux/xc_policy/contexts/files
}

IMAGE_PREPROCESS_COMMAND += "create_data_dir;"
