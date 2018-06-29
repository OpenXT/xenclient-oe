# XenClient UIVM image

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

IMAGE_FEATURES += " \
    package-management \
    read-only-rootfs \
"
IMAGE_FSTYPES = "ext3.vhd.gz"
export IMAGE_BASENAME = "xenclient-uivm-image"

COMPATIBLE_MACHINE = "(xenclient-uivm)"


BAD_RECOMMENDATIONS += " \
    avahi-daemon \
    avahi-autoipd \
"
# List of packages removed at rootfs-postprocess.
PACKAGE_REMOVE = " \
    busybox-hwclock \
"

XSERVER ?= "xserver-kdrive-fbdev"

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

IMAGE_FEATURES += "empty-root-password"

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
    libx11-locale \
    rsyslog \
    glibc-gconv-libjis \
    glibc-gconv-euc-jp \
    mobile-broadband-provider-info \
    shutdown-screen \
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    uim \
    uim-common \
    anthy \
    uim-gtk2.0 \
    matchbox-keyboard \
    matchbox-keyboard-im \
"

require xenclient-image-common.inc
require xenclient-version.inc
inherit xenclient-licences
inherit image

#zap root password for release images
ROOTFS_POSTPROCESS_COMMAND += '${@base_conditional("DISTRO_TYPE", "release", "zap_root_password; ", "",d)}'

post_rootfs_shell_commands() {
    # Start WM right away.
    echo 'x:5:respawn:/bin/su - root -c /usr/bin/startxfce4' >> ${IMAGE_ROOTFS}/etc/inittab

    # enable ctrlaltdel reboot because PV driver uses ctrl+alt+del to interpret reboot issued via xenstore
    echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab

    # Change root shell.
    sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd

    # Trick to resolve dom0 name with V4V.
    echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts

    # HACK: Force remove unwanted packages.
    # These should not be installed in the first place?
    opkg -f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} ${OPKG_ARGS} -force-depends remove ${PACKAGE_REMOVE}
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "

# Get a tty on hvc0 when in debug mode.
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "debug-tweaks", "start_tty_on_hvc0; ", "",d)}'

remove_nonessential_initscripts() {
    remove_initscript "finish.sh"
    remove_initscript "rmnologin.sh"
    remove_initscript "sshd"
    remove_initscript "urandom"
    remove_initscript "save-rtc.sh"
    remove_initscript "networking"
}
ROOTFS_POSTPROCESS_COMMAND += "remove_nonessential_initscripts; "
