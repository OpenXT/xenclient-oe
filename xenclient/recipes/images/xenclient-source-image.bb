# This image is used to build the XenClient source iso.
#
# Installs the source package for every recipe which needs to be built in
# order to build the other XenClient image recipes.

export IMAGE_BASENAME = "xenclient-source-image"

# The following list must be updated each time a new image is added:
PACKAGE_INSTALL = "\
    xenclient-initramfs-image-sources \
    xenclient-dom0-image-sources \
    xenclient-uivm-image-sources \
    xenclient-ndvm-image-sources \
    xenclient-syncvm-image-sources \
    xenclient-installer-image-sources \
    xenclient-installer-part2-image-sources"

IMAGE_BOOT = ""
IMAGE_FSTYPES = "raw"
IMAGE_INSTALL = ""
IMAGE_LINGUAS = ""
ONLINE_PACKAGE_MANAGEMENT = "none"

inherit image

ROOTFS_POSTPROCESS_COMMAND =+ " \
    rm -rf ${IMAGE_ROOTFS}/dev; \
    rm -rf ${IMAGE_ROOTFS}/etc; \
    rm -rf ${IMAGE_ROOTFS}/usr; \
    rm -rf ${IMAGE_ROOTFS}/Proprietary; \
    rm -rf ${IMAGE_ROOTFS}/[Uu]nknown; \
"

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe      \
                    file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# prevent ldconfig from being run
LDCONFIGDEPEND = ""
