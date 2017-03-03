# XenClient secure backend-domain image

include xenclient-image-common.inc
IMAGE_FEATURES += "package-management"

COMPATIBLE_MACHINE = "(xenclient-ndvm)"

IMAGE_FSTYPES = "xc.ext3.vhd.gz"

BAD_RECOMMENDATIONS += "avahi-daemon avahi-autoipd ca-certificates"
# The above seems to be broken and we *really* don't want avahi!
PACKAGE_REMOVE = "avahi-daemon avahi-autoipd hicolor-icon-theme"

export IMAGE_BASENAME = "xenclient-ndvm-image"

ANGSTROM_EXTRA_INSTALL += ""

DEPENDS = "packagegroup-base"
IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    modules \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    util-linux-mount \
    util-linux-umount \
    busybox \
    openssh \
    kernel-modules \
    libv4v \
    libv4v-bin \
    dbus \
    xenclient-dbusbouncer \
    networkmanager \
    xenclient-toolstack \
    linux-firmware-iwlwifi \
    linux-firmware-bnx2 \
    bridge-utils \
    iptables \
    xenclient-ndvm-tweaks \
    ipsec-tools \
    rsyslog \
    ${ANGSTROM_EXTRA_INSTALL} \
    xenclient-udev-force-discreet-net-to-eth0 \
    v4v-module \
    xen-libxenstore \
    xen-xenstore \
    wget \
    ethtool \
    carrier-detect \
    xenclient-nws \
    modemmanager \
    ppp \
    iputils-ping \
"

#IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

#zap root password for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_root_password; ", "",d)}'

post_rootfs_shell_commands() {
	sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd;

	echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts;

	# enable ctrlaltdel reboot because PV driver uses ctrl+alt+del to interpret reboot issued via xenstore
	echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab;

	# NDVM doesn't have a /dev/tty1, disable the login shell on it
	sed -i 's/[^#].*getty.*tty1$/#&/' ${IMAGE_ROOTFS}/etc/inittab ;

	# Move resolv.conf to /var/volatile/etc, as rootfs is readonly
	rm -f ${IMAGE_ROOTFS}/etc/resolv.conf; ln -s /var/volatile/etc/resolv.conf ${IMAGE_ROOTFS}/etc/resolv.conf;

	opkg -f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} ${OPKG_ARGS} -force-depends remove ${PACKAGE_REMOVE};
}

remove_initscripts() {
    # Remove unneeded initscripts
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/urandom ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/urandom
        update-rc.d -r ${IMAGE_ROOTFS} urandom remove
    fi
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/sshd ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/sshd
        update-rc.d -r ${IMAGE_ROOTFS} sshd remove
    fi
}

ROOTFS_POSTPROCESS_COMMAND += " post_rootfs_shell_commands; remove_initscripts; "

inherit openxt-selinux-image
#inherit validate-package-versions
inherit xenclient-image-src-info
inherit xenclient-image-src-package
inherit xenclient-licences
require xenclient-version.inc

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
