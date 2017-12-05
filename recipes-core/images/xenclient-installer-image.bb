# Part 1 of the XenClient host installer.
#
# This is responsible for retrieving the XenClient repository and extracting
# and running part 2 of the host installer, which contains the logic to install
# or upgrade a specific version of XenClient.

include xenclient-image-common.inc

COMPATIBLE_MACHINE = "(openxt-installer)"
IMAGE_INITSCRIPTS = "initscripts"

PR = "r15"

SRC_URI += " \
	    file://network.ans \
            file://network_upgrade.ans \
            file://network_manual.ans \
            file://network_download_win.ans \
            file://network_manual_download_win.ans \
	    file://pxelinux.cfg \
	    file://isolinux.cfg \
	    file://grub.cfg \
	    file://bootmsg.txt \
"

ANGSTROM_EXTRA_INSTALL += ""

export IMAGE_BASENAME = "xenclient-installer-image"

BAD_RECOMMENDATIONS += "${@base_contains('IMAGE_FEATURES', 'web-certificates', '', 'ca-certificates', d)}"

DEPENDS = "packagegroup-base packagegroup-xenclient-installer grub-efi-cross"

IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    ${IMAGE_INITSCRIPTS} \
    modules-installer \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    packagegroup-xenclient-installer \
    linux-firmware-iwlwifi \
    linux-firmware-bnx2 \
    linux-firmware-i915 \
    ${ANGSTROM_EXTRA_INSTALL}"

IMAGE_FSTYPES = "cpio.gz"

# IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

post_rootfs_shell_commands() {
	# Create /init symlink
	ln -s sbin/init ${IMAGE_ROOTFS}/init;

	# Update /etc/inittab
	sed -i '/^1:/d' ${IMAGE_ROOTFS}/etc/inittab; 
	{
		echo '1:2345:once:/install/part1/autostart-main < /dev/tty1 > /dev/tty1';
		echo '2:2345:respawn:/usr/bin/tail -F /var/log/installer > /dev/tty2';
		echo '3:2345:respawn:/sbin/getty 38400 tty3';
		echo '4:2345:respawn:/usr/bin/tail -F /var/log/messages > /dev/tty4';
		echo '5:2345:respawn:/sbin/getty 38400 tty5';
		echo '6:2345:respawn:/sbin/getty 38400 tty6';
		echo '7:2345:respawn:/install/part1/autostart-status < /dev/tty7 > /dev/tty7';
		echo 'ca::ctrlaltdel:/sbin/reboot';
	} >> ${IMAGE_ROOTFS}/etc/inittab;

	# Update /etc/fstab
	sed -i '/^\/dev\/mapper\/xenclient/d' ${IMAGE_ROOTFS}/etc/fstab;

	# Update /etc/network/interfaces
	{
		echo 'auto lo';
		echo 'iface lo inet loopback';
	} > ${IMAGE_ROOTFS}/etc/network/interfaces;

	# Password files are expected in /config
	mkdir -p ${IMAGE_ROOTFS}/config/etc;
	mv ${IMAGE_ROOTFS}/etc/shadow ${IMAGE_ROOTFS}/config/etc/shadow;
	mv ${IMAGE_ROOTFS}/etc/passwd ${IMAGE_ROOTFS}/config/etc/passwd;
	ln -s /config/etc/shadow ${IMAGE_ROOTFS}/etc/shadow;
	ln -s /config/etc/passwd ${IMAGE_ROOTFS}/etc/passwd;

	# Use bash as login shell
	sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/config/etc/passwd;

	# Don't start blktapctrl daemon
	rm -f ${IMAGE_ROOTFS}/etc/init.d/blktap; 
	rm -f ${IMAGE_ROOTFS}/etc/rc*.d/*blktap;

	# Create file to identify this as the host installer filesystem
	touch ${IMAGE_ROOTFS}/etc/xenclient-host-installer;
}

# packagegroup-xenclient-dom0 provides lvm2, so have lvmetad running as lvm2
# utilities try to use it and warn in its absence.
activate_lvmetad_initscript() {
    update-rc.d -r ${IMAGE_ROOTFS} lvm2-lvmetad defaults 06
}

ROOTFS_POSTPROCESS_COMMAND += " \
    activate_lvmetad_initscript; \
    post_rootfs_shell_commands; \
"

do_post_rootfs_items() {
	install -d ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot
	install -m 0644 ${IMAGE_ROOTFS}/${datadir}/syslinux/ldlinux.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
	install -m 0644 ${IMAGE_ROOTFS}/${datadir}/syslinux/mboot.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
	install -m 0644 ${IMAGE_ROOTFS}/${datadir}/syslinux/libcom32.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
	for i in ${WORKDIR}/*.ans ; do 
		install -m 0644 ${i} ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
	done
	install -m 0644 ${WORKDIR}/pxelinux.cfg ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
	install -d ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso
	install -m 0644 ${IMAGE_ROOTFS}/${datadir}/syslinux/ldlinux.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
	install -m 0644 ${IMAGE_ROOTFS}/${datadir}/syslinux/mboot.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
	install -m 0644 ${IMAGE_ROOTFS}/${datadir}/syslinux/libcom32.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
	install -m 0644 ${IMAGE_ROOTFS}/${datadir}/syslinux/pxelinux.0 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
	install -m 0644 ${WORKDIR}/bootmsg.txt ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
	install -m 0644 ${IMAGE_ROOTFS}/${datadir}/syslinux/isolinux.bin ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
	install -m 0644 ${WORKDIR}/isolinux.cfg ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
	install -m 0644 ${WORKDIR}/grub.cfg ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/

	# Force the copy in case this has already run before
	cp -f ${IMAGE_ROOTFS}/boot/tboot.gz ${DEPLOY_DIR_IMAGE}/
	cp -f ${IMAGE_ROOTFS}/boot/xen.gz ${DEPLOY_DIR_IMAGE}/
	cp -f ${IMAGE_ROOTFS}/boot/GM45_GS45_PM45_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/gm45.acm
	cp -f ${IMAGE_ROOTFS}/boot/4th_gen_i5_i7_SINIT_75.BIN ${DEPLOY_DIR_IMAGE}/hsw.acm
	cp -f ${IMAGE_ROOTFS}/boot/i5_i7_DUAL_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/duali.acm
	cp -f ${IMAGE_ROOTFS}/boot/i7_QUAD_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/quadi.acm
	cp -f ${IMAGE_ROOTFS}/boot/Q35_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/q35.acm
	cp -f ${IMAGE_ROOTFS}/boot/Q45_Q43_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/q45q43.acm
	cp -f ${IMAGE_ROOTFS}/boot/Xeon-5600-3500-SINIT-v1.1.bin ${DEPLOY_DIR_IMAGE}/xeon56.acm
	cp -f ${IMAGE_ROOTFS}/boot/Xeon-E7-8800-4800-2800-SINIT-v1.1.bin ${DEPLOY_DIR_IMAGE}/xeone7.acm
	cp -f ${IMAGE_ROOTFS}/boot/3rd_gen_i5_i7_SINIT_67.BIN ${DEPLOY_DIR_IMAGE}/ivb_snb.acm
	cp -f ${IMAGE_ROOTFS}/boot/5th_gen_i5_i7_SINIT_79.BIN ${DEPLOY_DIR_IMAGE}/bdw.acm
	cp -f ${IMAGE_ROOTFS}/boot/6th_gen_i5_i7_SINIT_71.BIN ${DEPLOY_DIR_IMAGE}/skl.acm
	cp -f ${IMAGE_ROOTFS}/boot/7th_gen_i5_i7-SINIT_74.bin ${DEPLOY_DIR_IMAGE}/kbl.acm
	cp -f ${IMAGE_ROOTFS}/boot/license-SINIT-ACMs.txt ${DEPLOY_DIR_IMAGE}/license-SINIT-ACMs.txt

	# Add the microcode to the installer
	cp -f ${IMAGE_ROOTFS}/boot/microcode_intel.bin ${DEPLOY_DIR_IMAGE}/microcode_intel.bin
}

addtask post_rootfs_items after do_rootfs before do_build

inherit image
inherit xenclient-image-src-info
inherit xenclient-image-src-package
inherit xenclient-licences
require xenclient-version.inc

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

python() {
    bb.data.delVarFlag("do_fetch", "noexec", d);
    bb.data.delVarFlag("do_unpack", "noexec", d);
    bb.data.delVarFlag("do_patch", "noexec", d);
    bb.data.delVarFlag("do_configure", "noexec", d);
    bb.data.delVarFlag("do_compile", "noexec", d);
    bb.data.delVarFlag("do_install", "noexec", d);
}
do_rootfs[depends] += "xenclient-installer-image:do_install"
