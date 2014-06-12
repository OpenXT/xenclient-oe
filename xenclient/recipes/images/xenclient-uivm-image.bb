# XenClient UIVM image

include xenclient-image-common.inc
IMAGE_FEATURES += "package-management"

COMPATIBLE_MACHINE = "(xenclient-uivm)"

IMAGE_FSTYPES = "xc.ext3.vhd.gz"

BAD_RECOMMENDATIONS += "avahi-daemon avahi-autoipd"
# The above seems to be broken and we *really* don't want avahi!
PACKAGE_REMOVE = "avahi-daemon avahi-autoipd"

ANGSTROM_EXTRA_INSTALL += " \
			  " 
XSERVER ?= "xserver-kdrive-fbdev"
#SPLASH ?= ' ${@base_contains("MACHINE_FEATURES", "screen", "psplash-angstrom", "",d)}'

export IMAGE_BASENAME = "xenclient-uivm-image"

DEPENDS = "task-base"
IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    ${XSERVER} \
    modules \
    task-xenclient-common \
    task-xenclient-xfce-minimal \
    openssh \
    task-core-boot \
    task-base \
    xenfb2 \
    kernel-modules \
    v4v-module \
    libv4v \
    libv4v-bin \
    xinit \
    xprop \
    xrandr \
    midori \
    network-manager-applet \
    network-manager-applet-locale-de \
    network-manager-applet-locale-es \
    network-manager-applet-locale-fr \
    network-manager-applet-locale-ja \
    network-manager-applet-locale-zh-cn \
    gtk+-locale-de \
    gtk+-locale-es \
    gtk+-locale-fr \
    gtk+-locale-ja \
    gtk+-locale-zh-cn \
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
    bootage \
    libx11-locale \
    rsyslog \
    glibc-gconv-libjis \
    glibc-gconv-euc-jp \
    glibc-localedata-translit-cjk-variants \
    glibc-localedata-ja-jp \
    locale-base-ja-jp \
    locale-base-de-de \
    locale-base-es-es \
    locale-base-fr-fr \
    locale-base-zh-cn \
    mobile-broadband-provider-info \
    shutdown-screen \
    fusechat \
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    uim \
    anthy \
    uim-gtk2.0 \
    matchbox-keyboard \
    matchbox-keyboard-im \
    ${ANGSTROM_EXTRA_INSTALL}"

# OE upgrade - temporarly disabled:

# angstrom-x11-base-depends \
# task-xfce46-base \
# angstrom-gnome-icon-theme-enable \
# battery-applet-4-xfce4 \
# battery-applet-4-xfce4-locale-de \
# battery-applet-4-xfce4-locale-es \
# battery-applet-4-xfce4-locale-fr \
# battery-applet-4-xfce4-locale-ja \
# battery-applet-4-xfce4-locale-zh-cn \
# xsetroot \
# gconf-dbus \
# xkbd \
#


#    angstrom-gpe-task-base \
#    ${SPLASH} \

#IMAGE_INSTALL = "\
#    ${XSERVER} \
#    task-base-extended \
#    coreutils \
#    bash \
#    angstrom-x11-base-depends \
#    angstrom-gpe-task-base \
#    angstrom-gpe-task-settings \
#    kernel-modules \
#    hal \
#    devilspie \
#    midori \
#    ${SPLASH} \
#    ${ANGSTROM_EXTRA_INSTALL}"

# IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

#zap root password for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_root_password; ", "",d)}'

ROOTFS_POSTPROCESS_COMMAND += "echo 'x:5:respawn:/bin/su - root -c /usr/bin/startxfce4' >> ${IMAGE_ROOTFS}/etc/inittab;"

# enable ctrlaltdel reboot because PV driver uses ctrl+alt+del to interpret reboot issued via xenstore
ROOTFS_POSTPROCESS_COMMAND += "echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab;"

ROOTFS_POSTPROCESS_COMMAND += "sed -i 's|root:x:0:0:root:/home/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd;"

ROOTFS_POSTPROCESS_COMMAND += "echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts;"

ROOTFS_POSTPROCESS_COMMAND += "opkg-cl ${IPKG_ARGS} -force-depends \
                                remove ${PACKAGE_REMOVE};"

# readonly rootfs prevents sshd from creating dirs
ROOTFS_POSTPROCESS_COMMAND += "mkdir ${IMAGE_ROOTFS}/root/.ssh;"

inherit image
#inherit validate-package-versions
inherit xenclient-image-src-info
inherit xenclient-image-src-package
inherit xenclient-licences
require xenclient-version.inc

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe      \
                    file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
