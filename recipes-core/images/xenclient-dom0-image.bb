# XenClient dom0 image

include xenclient-image-common.inc
IMAGE_FEATURES += "package-management"

COMPATIBLE_MACHINE = "(xenclient-dom0)"

IMAGE_FSTYPES = "xc.ext3.gz"

# No thanks, we provide our own xorg.conf with the hacked Intel driver
# And we don't need Avahi
# Nor a collection of questionable CA certificates
BAD_RECOMMENDATIONS += "xserver-xorg avahi-daemon avahi-autoipd ca-certificates"
# The above seems to be broken and we *really* don't want avahi!
PACKAGE_REMOVE = "avahi-daemon avahi-autoipd"

ANGSTROM_EXTRA_INSTALL += " \
			  " 
export IMAGE_BASENAME = "xenclient-dom0-image"
export STAGING_KERNEL_DIR

DEPENDS = "packagegroup-base packagegroup-xenclient-dom0"
IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    initscripts \
    modules \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    packagegroup-xenclient-dom0 \
    v4v-module \
    xenclient-preload-hs-libs \
    ${ANGSTROM_EXTRA_INSTALL}"

# IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

#zap root password for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_root_password; ", "",d)}'

post_rootfs_shell_commands() {
	# zap root password in shadow
	sed -i 's%^root:[^:]*:%root:*:%' ${IMAGE_ROOTFS}/etc/shadow;

	sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd;

	mkdir -p ${IMAGE_ROOTFS}/config/etc;
	mv ${IMAGE_ROOTFS}/etc/passwd ${IMAGE_ROOTFS}/config/etc;
	mv ${IMAGE_ROOTFS}/etc/shadow ${IMAGE_ROOTFS}/config/etc;
	ln -s ../config/etc/passwd ${IMAGE_ROOTFS}/etc/passwd;
	ln -s ../config/etc/shadow ${IMAGE_ROOTFS}/etc/shadow;
	ln -s ../config/etc/.pwd.lock ${IMAGE_ROOTFS}/etc/.pwd.lock;
	ln -s ../var/volatile/etc/asound ${IMAGE_ROOTFS}/etc/asound;

	rm ${IMAGE_ROOTFS}/etc/hosts; ln -s /var/run/hosts ${IMAGE_ROOTFS}/etc/hosts;
	ln -s /var/volatile/etc/resolv.conf ${IMAGE_ROOTFS}/etc/resolv.conf;

	echo 'kernel.printk_ratelimit = 0' >> ${IMAGE_ROOTFS}/etc/sysctl.conf;

	# Add initramfs
	cat ${DEPLOY_DIR_IMAGE}/xenclient-initramfs-image-xenclient-dom0.cpio.gz > ${IMAGE_ROOTFS}/boot/initramfs.gz ;

	sed -i 's|1:2345:respawn:/sbin/getty 38400 tty1|#1:2345:respawn:/sbin/getty 38400 tty1|' ${IMAGE_ROOTFS}/etc/inittab ;

	# Add dom0 console getty
	echo '1:2345:respawn:/sbin/getty 38400 tty1' >> ${IMAGE_ROOTFS}/etc/inittab ;

	# Create mountpoint for /mnt/secure
	mkdir -p ${IMAGE_ROOTFS}/mnt/secure ;

	# Create mountpoint for /mnt/upgrade
	mkdir -p ${IMAGE_ROOTFS}/mnt/upgrade ;

	# Create mountpoint for boot/system
	mkdir -p ${IMAGE_ROOTFS}/boot/system ;

	# Remove unwanted packages specified above
	opkg -f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} ${OPKG_ARGS} -force-depends remove ${PACKAGE_REMOVE};

	# Remove network modules except netfront
	for x in `find ${IMAGE_ROOTFS}/lib/modules -name *.ko | grep drivers/net | grep -v xen-netfront`; do
		pkg="kernel-module-`basename $x .ko | sed s/_/-/g`";
		opkg ${IPKG_ARGS} -force-depends remove $pkg;
	done;

	# Write coredumps in /var/cores
	echo 'kernel.core_pattern = /var/cores/%e-%t.%p.core' >> ${IMAGE_ROOTFS}/etc/sysctl.conf ;
}

### Stubdomain stuff - temporary
STUBDOMAIN_DEPLOY_DIR_IMAGE = "${DEPLOY_DIR}/images/xenclient-stubdomain"
STUBDOMAIN_IMAGE = "${STUBDOMAIN_DEPLOY_DIR_IMAGE}/xenclient-stubdomain-initramfs-image-xenclient-stubdomain.cpio.gz"
STUBDOMAIN_KERNEL = "${STUBDOMAIN_DEPLOY_DIR_IMAGE}/bzImage-xenclient-stubdomain.bin"
process_tmp_stubdomain_items() {
	mkdir -p ${IMAGE_ROOTFS}/usr/lib/xen/boot ;
	cat ${STUBDOMAIN_IMAGE} > ${IMAGE_ROOTFS}/usr/lib/xen/boot/stubdomain-initramfs ;
	cat ${STUBDOMAIN_KERNEL} > ${IMAGE_ROOTFS}/usr/lib/xen/boot/stubdomain-bzImage ; 
}

# Get rid of unneeded initscripts
remove_initscripts() {
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/hostname.sh ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/hostname.sh
        update-rc.d -r ${IMAGE_ROOTFS} hostname.sh remove
    fi

    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/rmnologin.sh ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/rmnologin.sh
        update-rc.d -r ${IMAGE_ROOTFS} rmnologin.sh remove
    fi

    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/finish.sh ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/finish.sh
        update-rc.d -r ${IMAGE_ROOTFS} finish.sh remove
    fi

    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/mount-special ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/mount-special
        update-rc.d -r ${IMAGE_ROOTFS} mount-special remove
    fi
}

ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_shell_commands; remove_initscripts; process_tmp_stubdomain_items; "

inherit openxt-selinux-image
#inherit validate-package-versions
inherit xenclient-image-src-info
inherit xenclient-image-src-package
inherit xenclient-licences
require xenclient-version.inc

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
