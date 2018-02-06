# XenClient UIVM image

include xenclient-image-common.inc
IMAGE_FEATURES += " \
    package-management \
    read-only-rootfs \
"

COMPATIBLE_MACHINE = "(xenclient-uivm)"

IMAGE_FSTYPES = "ext3.vhd.gz"

BAD_RECOMMENDATIONS += " \
    avahi-daemon \
    avahi-autoipd \
"
# The above seems to be broken and we *really* don't want avahi!
PACKAGE_REMOVE = " \
    avahi-daemon \
    avahi-autoipd \
    busybox-hwclock \
"

XSERVER ?= "xserver-kdrive-fbdev"

export IMAGE_BASENAME = "xenclient-uivm-image"

DEPENDS = "packagegroup-base"

# Specifies the list of locales to install into the image during the root
# filesystem construction process.
# http://www.yoctoproject.org/docs/current/ref-manual/ref-manual.html#var-IMAGE_LINGUAS
IMAGE_LINGUAS = " \
    de-de \
    en-us \
    es-es \
    fr-fr \
    ja-jp \
    zh-cn \
"

IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    ${XSERVER} \
    modules \
    packagegroup-xenclient-common \
    packagegroup-xenclient-xfce-minimal \
    openssh \
    packagegroup-core-boot \
    packagegroup-base \
    xenfb2 \
    kernel-modules \
    v4v-module \
    libv4v \
    libv4v-bin \
    xinit \
    xprop \
    xrandr \
    surf \
    network-manager-applet \
    network-manager-applet-locale-de \
    network-manager-applet-locale-es \
    network-manager-applet-locale-fr \
    network-manager-applet-locale-ja \
    network-manager-applet-locale-zh-cn \
    gnome-keyring-locale-de \
    gnome-keyring-locale-es \
    gnome-keyring-locale-fr \
    gnome-keyring-locale-ja \
    gnome-keyring-locale-zh-cn \
    iso-codes-locale-de \
    iso-codes-locale-es \
    iso-codes-locale-fr \
    iso-codes-locale-ja \
    iso-codes-locale-zh-cn \
    xterm \
    gconf \
    xblanker \
    xenclient-uivm-xsessionconfig \
    setxkbmap \
    resized \
    xenclient-keymap-sync \
    libx11-locale \
    rsyslog \
    glibc-gconv-libjis \
    glibc-gconv-euc-jp \
    mobile-broadband-provider-info \
    shutdown-screen \
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    uim \
    anthy \
    uim-gtk2.0 \
    matchbox-keyboard \
    matchbox-keyboard-im \
"

#zap root password for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_root_password; ", "",d)}'

post_rootfs_shell_commands() {
	echo 'x:5:respawn:/bin/su - root -c /usr/bin/startxfce4' >> ${IMAGE_ROOTFS}/etc/inittab;

	# enable ctrlaltdel reboot because PV driver uses ctrl+alt+del to interpret reboot issued via xenstore
	echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab;

	sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd;

	echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts;

	opkg -f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} ${OPKG_ARGS} -force-depends remove ${PACKAGE_REMOVE}

	# readonly rootfs prevents sshd from creating dirs
	mkdir ${IMAGE_ROOTFS}/root/.ssh;
	
	mkdir ${IMAGE_ROOTFS}/root/.cache;
}

remove_initscripts() {
    # Remove unneeded initscripts
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/finish.sh ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/finish.sh
        update-rc.d -r ${IMAGE_ROOTFS} finish.sh remove
    fi
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/rmnologin.sh ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/rmnologin.sh
        update-rc.d -r ${IMAGE_ROOTFS} rmnologin.sh remove
    fi
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/sshd ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/sshd
        update-rc.d -r ${IMAGE_ROOTFS} sshd remove
    fi
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/urandom ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/urandom
        update-rc.d -r ${IMAGE_ROOTFS} urandom remove
    fi
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/save-rtc.sh ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/save-rtc.sh
        update-rc.d -r ${IMAGE_ROOTFS} save-rtc.sh remove
    fi
    if [ -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/networking ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/networking
        update-rc.d -r ${IMAGE_ROOTFS} networking remove
    fi
}

ROOTFS_POSTPROCESS_COMMAND += " \
     post_rootfs_shell_commands; \
     remove_initscripts; \
"

inherit image
inherit xenclient-licences
require xenclient-version.inc

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
