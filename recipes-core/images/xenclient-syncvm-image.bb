# XenClient Synchronizer client VM image

include xenclient-image-common.inc
IMAGE_FEATURES += "package-management"

COMPATIBLE_MACHINE = "(xenclient-syncvm)"

IMAGE_FSTYPES = "xc.ext3.vhd.gz"

ANGSTROM_EXTRA_INSTALL += ""

export IMAGE_BASENAME = "xenclient-syncvm-image"

DEPENDS = "packagegroup-base"

IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    modules \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    kernel-modules \
    v4v-module \
    libv4v \
    libv4v-bin \
    rsyslog \
    openssh \
    blktap3 \
    wget \
    sync-client \
    ifplugd \
    xenclient-syncvm-tweaks \
    ${ANGSTROM_EXTRA_INSTALL}"

#IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

post_rootfs_shell_commands() {
	echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab;
	sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd;
	echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts;
	rm -f ${IMAGE_ROOTFS}/etc/resolv.conf;
	ln -s /var/volatile/etc/resolv.conf ${IMAGE_ROOTFS}/etc/resolv.conf;
	rm -f ${IMAGE_ROOTFS}/etc/network/interfaces;
	ln -s /var/volatile/etc/network/interfaces ${IMAGE_ROOTFS}/etc/network/interfaces;
}

remove_initscripts() {
    # Remove unneeded initscripts
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/urandom ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/urandom
        update-rc.d -r ${IMAGE_ROOTFS} urandom remove
    fi
}

ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_shell_commands; remove_initscripts; "

inherit image
#inherit validate-package-versions
inherit xenclient-image-src-info
inherit xenclient-image-src-package
inherit xenclient-licences
require xenclient-version.inc

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
