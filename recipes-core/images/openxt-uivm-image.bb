# XenClient UIVM image

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

inherit openxt-image

IMAGE_FEATURES += " \
    package-management \
    read-only-rootfs \
    empty-root-password \
    root-bash-shell \
"
IMAGE_FSTYPES = "ext3.vhd.gz"
export IMAGE_BASENAME = "openxt-uivm-image"

COMPATIBLE_MACHINE = "(openxt-uivm)"


BAD_RECOMMENDATIONS += " \
    avahi-daemon \
    avahi-autoipd \
"

INITSCRIPT_REMOVE = " \
    finish.sh \
    rmnologin.sh \
    sshd \
    urandom \
    networking \
"

# Specifies the list of locales to install into the image during the root
# filesystem construction process.
# http://www.yoctoproject.org/docs/current/ref-manual/ref-manual.html#var-IMAGE_LINGUAS
IMAGE_LINGUAS = " \
    en-us \
"

# Refine xserver packages installed by packagegroup-core-x11-xserver.
XSERVER = " \
    xserver-xorg \
    xf86-input-evdev \
    xf86-input-mouse \
    xf86-input-keyboard \
    xf86-video-openxtfb \
"

IMAGE_INSTALL += "\
    ${XSERVER} \
    packagegroup-xenclient-common \
    packagegroup-xenclient-xfce-minimal \
    openssh \
    packagegroup-base \
    kernel-modules \
    argo-module \
    libargo \
    libargo-bin \
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
    xenclient-uivm-xsessionconfig \
    setxkbmap \
    libx11-locale \
    rsyslog \
    glibc-gconv-libjis \
    glibc-gconv-euc-jp \
    mobile-broadband-provider-info \
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    uim \
    uim-common \
    uim-gtk2.0 \
    anthy \
    matchbox-keyboard \
    matchbox-keyboard-im \
    kernel-module-openxtfb \
"

require xenclient-version.inc
inherit xenclient-licences

post_rootfs_shell_commands() {
    # Start WM right away.
    echo 'x:5:respawn:/bin/su - root -c /usr/bin/startxfce4' >> ${IMAGE_ROOTFS}/etc/inittab

    # enable ctrlaltdel reboot because PV driver uses ctrl+alt+del to interpret reboot issued via xenstore
    echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab

    # Trick to resolve dom0 name with argo.
    echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "
