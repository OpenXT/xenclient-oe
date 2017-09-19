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

post_rootfs_shell_commands() {
	mv ${IMAGE_ROOTFS}/etc/xenclient.conf ${IMAGE_ROOTFS}/config/;
	rm -rf ${IMAGE_ROOTFS}/dev;
	rm -rf ${IMAGE_ROOTFS}/etc;
	rm -rf ${IMAGE_ROOTFS}/usr;
	rm -rf ${IMAGE_ROOTFS}/bin;
	rm -rf ${IMAGE_ROOTFS}/lib;
	rm -rf ${IMAGE_ROOTFS}/sbin;
	# safe to now remove the run directory and put the script there
	rm -rf ${IMAGE_ROOTFS}/run;
	mv -f ${IMAGE_ROOTFS}/run.installer ${IMAGE_ROOTFS}/run;
}

ROOTFS_POSTPROCESS_COMMAND += " \
    post_rootfs_shell_commands; \
"

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# prevent ldconfig from being run
LDCONFIGDEPEND = ""
