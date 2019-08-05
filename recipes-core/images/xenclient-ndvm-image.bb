# XenClient Network VM image.

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

inherit openxt-selinux-image

IMAGE_FEATURES += " \
    package-management \
    read-only-rootfs \
    empty-root-password \
    root-bash-shell \
"

IMAGE_FSTYPES = "ext3.disk.vhd.gz"
export IMAGE_BASENAME = "xenclient-ndvm-image"

COMPATIBLE_MACHINE = "(xenclient-ndvm)"


BAD_RECOMMENDATIONS += " \
    avahi-daemon \
    avahi-autoipd \
    ca-certificates \
"
# List of packages that should not be installed
PACKAGE_REMOVE = " \
    hicolor-icon-theme \
"

IMAGE_FEATURES += "empty-root-password"

INITSCRIPT_REMOVE = " \
    urandom \
    sshd \
"

IMAGE_INSTALL = " \
    ${ROOTFS_PKGMANAGE} \
    modules-ndvm \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    util-linux-mount \
    util-linux-umount \
    openssh \
    kernel-modules \
    libargo \
    libargo-bin \
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
    xenclient-udev-force-discreet-net-to-eth0 \
    argo-module \
    xen-libxenstore \
    xen-xenstore \
    xen-ocaml-libs \
    wget \
    ethtool \
    carrier-detect \
    xenclient-nws \
    modemmanager \
    ppp \
    iputils-ping \
"

require xenclient-version.inc
inherit xenclient-licences

post_rootfs_shell_commands() {
    # Trick to resolve dom0 name with argo.
    echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts;

    # enable ctrlaltdel reboot because PV driver uses ctrl+alt+del to interpret reboot issued via xenstore
    echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab;

    # NDVM doesn't have a /dev/tty1, disable the login shell on it
    sed -i 's/[^#].*getty.*tty1$/#&/' ${IMAGE_ROOTFS}/etc/inittab ;
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "
