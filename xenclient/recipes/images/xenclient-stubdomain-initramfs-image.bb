# initramfs image allowing to boot from location as specified on kernel
# command line, from teh choices of block device, loop back images (including
# recursive) and NFS.

COMPATIBLE_MACHINE = "(xenclient-stubdomain)"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "dialog"
 
IMAGE_FSTYPES = "cpio.gz"
IMAGE_INSTALL = " busybox bridge-utils initramfs-xenclient "
IMAGE_INSTALL += " ioemu dm-wrapper-stubdom v4v-module dm-agent-stubdom simple-poweroff "
IMAGE_LINGUAS = ""
IMAGE_DEV_MANAGER = "busybox-mdev"
IMAGE_BOOT = "${IMAGE_DEV_MANAGER}"
# Install only ${IMAGE_INSTALL}, not even deps
PACKAGE_INSTALL_NO_DEPS = "1"

# Remove any kernel-image that the kernel-module-* packages may have pulled in.
PACKAGE_REMOVE = "kernel-image-* update-modules udev sysvinit opkg-cl"

ROOTFS_POSTPROCESS_COMMAND += "opkg-cl ${IPKG_ARGS} -force-depends \
                                remove ${PACKAGE_REMOVE}; \
				rm -f ${IMAGE_ROOTFS}/sbin/udhcpc; \
				rm -f ${IMAGE_ROOTFS}/sbin/ldconfig; \
				rm -rvf ${IMAGE_ROOTFS}/usr/lib/opkg; \
"

inherit image
#inherit validate-package-versions
inherit xenclient-image-src-info
inherit xenclient-image-src-package

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe      \
                    file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
