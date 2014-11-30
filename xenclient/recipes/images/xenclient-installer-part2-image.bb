# Part 2 of the XenClient host installer.
#
# This contains the logic to install or upgrade a specific version of
# XenClient. The resulting image is copied to the control.tar.bz2 file
# in the XenClient repository.

COMPATIBLE_MACHINE = "(xenclient-dom0)"

export IMAGE_BASENAME = "xenclient-installer-part2-image"

PACKAGE_INSTALL = "xenclient-installer-part2"

IMAGE_BOOT = ""
IMAGE_FSTYPES = "tar.bz2"
IMAGE_INSTALL = ""
IMAGE_LINGUAS = ""
ONLINE_PACKAGE_MANAGEMENT = "none"

inherit image
inherit xenclient-image-src-info
inherit xenclient-image-src-package
inherit xenclient-licences
require xenclient-version.inc

ROOTFS_POSTPROCESS_COMMAND += " \
    mv ${IMAGE_ROOTFS}/etc/xenclient.conf ${IMAGE_ROOTFS}/config/; \
    rm -rf ${IMAGE_ROOTFS}/dev; \
    rm -rf ${IMAGE_ROOTFS}/etc; \
    rm -rf ${IMAGE_ROOTFS}/usr;"

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=751419260aa954499f7abaabaa882bbe      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# prevent ldconfig from being run
LDCONFIGDEPEND = ""
