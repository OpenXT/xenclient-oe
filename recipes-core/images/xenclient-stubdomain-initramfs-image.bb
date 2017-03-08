# initramfs image allowing to boot from location as specified on kernel
# command line, from teh choices of block device, loop back images (including
# recursive) and NFS.

COMPATIBLE_MACHINE = "(xenclient-stubdomain)"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "dialog xen"
 
IMAGE_FSTYPES = "cpio.gz"
IMAGE_INSTALL = "busybox bridge-utils initramfs-xenclient xen-xenstore"
IMAGE_INSTALL += "qemu-dm-stubdom v4v-module dm-agent simple-poweroff rsyslog"
IMAGE_LINGUAS = ""
IMAGE_DEV_MANAGER = "busybox-mdev"
IMAGE_BOOT = "${IMAGE_DEV_MANAGER}"
# Install only ${IMAGE_INSTALL}, not even deps
PACKAGE_INSTALL_NO_DEPS = "1"

# Remove any kernel-image that the kernel-module-* packages may have pulled in.
PACKAGE_REMOVE = "kernel-image-* update-modules udev sysvinit opkg-cl"

post_rootfs_shell_commands() {
	opkg -f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} ${OPKG_ARGS} -force-depends remove ${PACKAGE_REMOVE};

	rm -f ${IMAGE_ROOTFS}/sbin/udhcpc;
	rm -f ${IMAGE_ROOTFS}/sbin/ldconfig;
	rm -rvf ${IMAGE_ROOTFS}/usr/lib/opkg;
}

ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_shell_commands; "

inherit image
#inherit validate-package-versions
inherit xenclient-image-src-info
inherit xenclient-image-src-package

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
