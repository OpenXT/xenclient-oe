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
    ctrlaltdel-reboot \
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
    linux-firmware-iwlwifi \
    linux-firmware-bnx2 \
    xenclient-ndvm-tweaks \
    rsyslog \
    xen-tools-libxenstore \
    xen-tools-xenstore \
    wget \
    ethtool \
    xenclient-nws \
    modemmanager \
    ppp \
    iputils-ping \
    dbd-tools-vm \
    xen-vif-scripts-ndvm \
    grub-xen-conf \
"

require xenclient-version.inc
inherit xenclient-licences

post_rootfs_shell_commands() {
    # Trick to resolve dom0 name with argo.
    echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts;

    # NDVM doesn't have a /dev/tty1, disable the login shell on it
    sed -i 's/[^#].*getty.*tty1$/#&/' ${IMAGE_ROOTFS}/etc/inittab ;
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "
