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

post_rootfs_shell_commands() {
	rm -rf ${IMAGE_ROOTFS}/dev;
	rm -rf ${IMAGE_ROOTFS}/etc;
	rm -rf ${IMAGE_ROOTFS}/usr;
	rm -rf ${IMAGE_ROOTFS}/Proprietary;
	rm -rf ${IMAGE_ROOTFS}/[Uu]nknown;
}

ROOTFS_POSTPROCESS_COMMANDS += " post_rootfs_shell_commands; "

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# prevent ldconfig from being run
LDCONFIGDEPEND = ""
