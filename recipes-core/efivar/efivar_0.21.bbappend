LICENSE = "GPLv2"
FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://linux_nvme_ioctl.patch \
"
