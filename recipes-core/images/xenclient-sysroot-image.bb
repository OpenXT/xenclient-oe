# XenClient sysroot image

include xenclient-image-common.inc
IMAGE_FEATURES += "package-management"

COMPATIBLE_MACHINE = "(xenclient-dom0)"

IMAGE_FSTYPES = "cpio.bz2"

# No thanks, we provide our own xorg.conf with the hacked Intel driver
# And we don't need Avahi
BAD_RECOMMENDATIONS += "xserver-xorg avahi-daemon avahi-autoipd"
# The above seems to be broken and we *really* don't want avahi!
PACKAGE_REMOVE = "avahi-daemon avahi-autoipd"

ANGSTROM_EXTRA_INSTALL += " \
			  " 
export IMAGE_BASENAME = "xenclient-sysroot-image"
export STAGING_KERNEL_DIR

DEPENDS = "packagegroup-base packagegroup-xenclient-dom0"
IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    initscripts \
    modules \
    packagegroup-base \
    packagegroup-core-boot \
    packagegroup-xenclient-common \
    packagegroup-xenclient-dom0 \
    essential-target-builddepends \
    ${ANGSTROM_EXTRA_INSTALL}"

#IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

#zap root password for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_root_password; ", "",d)}'

post_rootfs_shell_commands() {
	sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd;

	rm ${IMAGE_ROOTFS}/etc/hosts; ln -s /tmp/hosts ${IMAGE_ROOTFS}/etc/hosts;

	# Add initramfs
	cat ${DEPLOY_DIR_IMAGE}/xenclient-initramfs-image-xenclient-dom0.cpio.gz > ${IMAGE_ROOTFS}/boot/initramfs.gz ;

	sed -i 's|1:2345:respawn:/sbin/getty 38400 tty1|#1:2345:respawn:/sbin/getty 38400 tty1|' ${IMAGE_ROOTFS}/etc/inittab ;

	# Add input demon to inittab (temp hack)
	echo 'xi:5:respawn:/usr/bin/input_server >/dev/null 2>&1' >> ${IMAGE_ROOTFS}/etc/inittab ;
	# Same with surfman
	echo 'xs:5:respawn:/usr/bin/watch_surfman >/dev/null 2>&1' >> ${IMAGE_ROOTFS}/etc/inittab ;

	# Add dom0 console getty
	echo '1:2345:respawn:/sbin/getty 38400 tty1' >> ${IMAGE_ROOTFS}/etc/inittab ;

	# Create mountpoint for /mnt/secure
	mkdir -p ${IMAGE_ROOTFS}/mnt/secure ;

	# Create mountpoint for boot/system
	mkdir -p ${IMAGE_ROOTFS}/boot/system ;

	# Remove unwanted packages specified above
	opkg -f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} ${OPKG_ARGS} -force-depends remove ${PACKAGE_REMOVE};

	# Write coredumps in /var/cores
	echo 'kernel.core_pattern = /var/cores/%e-%t.%p.core' >> ${IMAGE_ROOTFS}/etc/sysctl.conf ;
}

# Get rid of unneeded initscripts
remove_initscripts() {
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/rmnologin.sh ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/rmnologin.sh
        update-rc.d -r ${IMAGE_ROOTFS} rmnologin.sh remove
    fi

    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/finish.sh ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/finish.sh
        update-rc.d -r ${IMAGE_ROOTFS} finish.sh remove
    fi
}

ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_shell_commands; remove_initscripts; "

inherit image
inherit xenclient-licences
require xenclient-version.inc

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
