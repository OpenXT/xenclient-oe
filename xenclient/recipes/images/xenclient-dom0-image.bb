# XenClient dom0 image

include xenclient-image-common.inc
IMAGE_FEATURES += "package-management"

COMPATIBLE_MACHINE = "(xenclient-dom0)"
IMAGE_INITSCRIPTS = "xenclient-dom0-initscripts"

IMAGE_FSTYPES = "xc.ext3.gz"

# No thanks, we provide our own xorg.conf with the hacked Intel driver
# And we don't need Avahi
BAD_RECOMMENDATIONS += "xserver-xorg avahi-daemon avahi-autoipd"
# The above seems to be broken and we *really* don't want avahi!
PACKAGE_REMOVE = "avahi-daemon avahi-autoipd"

ANGSTROM_EXTRA_INSTALL += " \
			  " 
export IMAGE_BASENAME = "xenclient-dom0-image"
export STAGING_KERNEL_DIR

DEPENDS = "task-base task-xenclient-dom0 task-xenclient-dom0-extra"
IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    ${IMAGE_INITSCRIPTS} \
    modules \
    task-core-boot \
    task-base \
    task-xenclient-common \
    task-xenclient-dom0 \
    v4v-module \
    xenclient-preload-hs-libs \
    ${ANGSTROM_EXTRA_INSTALL}"

# IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

#zap root password for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_root_password; ", "",d)}'

# zap root password in shadow
ROOTFS_POSTPROCESS_COMMAND += "sed -i 's%^root:[^:]*:%root:*:%' ${IMAGE_ROOTFS}/etc/shadow;"

ROOTFS_POSTPROCESS_COMMAND += "sed -i 's|root:x:0:0:root:/home/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd;"

ROOTFS_POSTPROCESS_COMMAND += " \
    mkdir -p ${IMAGE_ROOTFS}/config/etc; \
    mv ${IMAGE_ROOTFS}/etc/passwd ${IMAGE_ROOTFS}/config/etc; \
    mv ${IMAGE_ROOTFS}/etc/shadow ${IMAGE_ROOTFS}/config/etc; \
    ln -s ../config/etc/passwd ${IMAGE_ROOTFS}/etc/passwd; \
    ln -s ../config/etc/shadow ${IMAGE_ROOTFS}/etc/shadow; \
    ln -s ../config/etc/.pwd.lock ${IMAGE_ROOTFS}/etc/.pwd.lock; \
    ln -s ../var/volatile/etc/asound ${IMAGE_ROOTFS}/etc/asound; \
"

ROOTFS_POSTPROCESS_COMMAND += "rm ${IMAGE_ROOTFS}/etc/hosts; ln -s /var/run/hosts ${IMAGE_ROOTFS}/etc/hosts;"

ROOTFS_POSTPROCESS_COMMAND += "ln -s /var/volatile/etc/resolv.conf ${IMAGE_ROOTFS}/etc/resolv.conf;"

ROOTFS_POSTPROCESS_COMMAND += "echo 'kernel.printk_ratelimit = 0' >> ${IMAGE_ROOTFS}/etc/sysctl.conf;"

# Add initramfs
ROOTFS_POSTPROCESS_COMMAND += "cat ${DEPLOY_DIR_IMAGE}/xenclient-initramfs-image-xenclient-dom0.cpio.gz > ${IMAGE_ROOTFS}/boot/initramfs.gz ;" 

ROOTFS_POSTPROCESS_COMMAND += "sed -i 's|1:2345:respawn:/sbin/getty 38400 tty1|#1:2345:respawn:/sbin/getty 38400 tty1|' ${IMAGE_ROOTFS}/etc/inittab ;" 

# Add dom0 console getty
ROOTFS_POSTPROCESS_COMMAND += "echo '1:2345:respawn:/sbin/getty 38400 tty1' >> ${IMAGE_ROOTFS}/etc/inittab ;"

# Create mountpoint for /mnt/secure
ROOTFS_POSTPROCESS_COMMAND += "mkdir -p ${IMAGE_ROOTFS}/mnt/secure ;"

# Create mountpoint for /mnt/upgrade
ROOTFS_POSTPROCESS_COMMAND += "mkdir -p ${IMAGE_ROOTFS}/mnt/upgrade ;"

# Create mountpoint for boot/system
ROOTFS_POSTPROCESS_COMMAND += "mkdir -p ${IMAGE_ROOTFS}/boot/system ;"

# Remove unwanted packages specified above
ROOTFS_POSTPROCESS_COMMAND += "opkg-cl ${IPKG_ARGS} -force-depends \
                                remove ${PACKAGE_REMOVE};"

# Remove network modules except netfront
ROOTFS_POSTPROCESS_COMMAND += "\
  for x in `find ${IMAGE_ROOTFS}/lib/modules -name *.ko | grep drivers/net | grep -v xen-netfront`; do \
    pkg="kernel-module-`basename $x .ko | sed s/_/-/g`"; \
    opkg-cl ${IPKG_ARGS} -force-depends remove $pkg; \
  done; \
"


# Write coredumps in /var/cores
ROOTFS_POSTPROCESS_COMMAND += "echo 'kernel.core_pattern = /var/cores/%e-%t.%p.core' >> ${IMAGE_ROOTFS}/etc/sysctl.conf ;"

### Stubdomain stuff - temporary
STUBDOMAIN_DEPLOY_DIR_IMAGE = "${DEPLOY_DIR_IMAGE}"
STUBDOMAIN_IMAGE = "${STUBDOMAIN_DEPLOY_DIR_IMAGE}/xenclient-stubdomain-initramfs-image-xenclient-stubdomain.cpio.gz"
STUBDOMAIN_KERNEL = "${STUBDOMAIN_DEPLOY_DIR_IMAGE}/vmlinuz-xenclient-stubdomain.bin"
ROOTFS_POSTPROCESS_COMMAND += "mkdir -p ${IMAGE_ROOTFS}/usr/lib/xen/boot ;"
ROOTFS_POSTPROCESS_COMMAND += "cat ${STUBDOMAIN_IMAGE} > ${IMAGE_ROOTFS}/usr/lib/xen/boot/stubdomain-initramfs ;" 
ROOTFS_POSTPROCESS_COMMAND += "cat ${STUBDOMAIN_KERNEL} > ${IMAGE_ROOTFS}/usr/lib/xen/boot/stubdomain-bzImage ;" 
### End of stubdomain stuff

# Do SELinux labelling after all rootfs postprocessing commands.
IMAGE_PREPROCESS_COMMAND_append = " setfiles -r ${IMAGE_ROOTFS} ${IMAGE_ROOTFS}/etc/selinux/xc_policy/contexts/files/file_contexts ${IMAGE_ROOTFS} || exit 1;"

inherit selinux-image
#inherit validate-package-versions
inherit xenclient-image-src-info
inherit xenclient-image-src-package
inherit xenclient-licences
require xenclient-version.inc

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe      \
                    file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
