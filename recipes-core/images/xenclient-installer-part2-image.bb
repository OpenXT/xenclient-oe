# Part 2 of the XenClient host installer.
#
# This contains the logic to install or upgrade a specific version of
# XenClient. The resulting image is copied to the control.tar.bz2 file
# in the XenClient repository.

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

IMAGE_FSTYPES = "tar.bz2"
export IMAGE_BASENAME = "xenclient-installer-part2-image"

COMPATIBLE_MACHINE = "(openxt-installer)"

# PACKAGE_INSTALL is usualy set from the IMAGE_INSTALL variable with some
# additional items. Installer-part2 is barely an image in that regard, and
# should not include default packages.
PACKAGE_INSTALL = "xenclient-installer-part2"

IMAGE_INSTALL = ""

inherit image
inherit xenclient-licences

require xenclient-version.inc

# Installer part2 is not an actual rootfs. So mangle the usual FHS layout to
# leave only what is required.
pre_image_mangle() {
    mv ${IMAGE_ROOTFS}/etc/xenclient.conf ${IMAGE_ROOTFS}/config/xenclient.conf
    rm -rf ${IMAGE_ROOTFS}/dev
    rm -rf ${IMAGE_ROOTFS}/etc
    rm -rf ${IMAGE_ROOTFS}/usr
    rm -rf ${IMAGE_ROOTFS}/bin
    rm -rf ${IMAGE_ROOTFS}/lib
    rm -rf ${IMAGE_ROOTFS}/sbin
    # "safe" to now remove the run directory and put the script there
    rm -rf ${IMAGE_ROOTFS}/run
    mv -f ${IMAGE_ROOTFS}/run.installer ${IMAGE_ROOTFS}/run
}
IMAGE_PREPROCESS_COMMAND += " \
    pre_image_mangle; \
"

# prevent ldconfig from being run
LDCONFIGDEPEND = ""
