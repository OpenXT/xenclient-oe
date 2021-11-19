# XenClient Synchronizer client VM image

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

inherit openxt-image

IMAGE_FEATURES += " \
    package-management \
    read-only-rootfs \
    root-bash-shell \
"

IMAGE_FSTYPES = "ext3.vhd.gz"
export IMAGE_BASENAME = "xenclient-syncvm-image"

COMPATIBLE_MACHINE = "(xenclient-syncvm)"

INITSCRIPT_REMOVE = " \
    finish.sh \
    rmnologin.sh \
    urandom \
"

IMAGE_INSTALL = "\
    ${ROOTFS_PKGMANAGE} \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    kernel-modules \
    argo-module \
    libargo \
    libargo-bin \
    rsyslog \
    openssh \
    wget \
    sync-client \
    xenclient-syncvm-tweaks \
    blktap3 \
"

require xenclient-version.inc
inherit xenclient-licences

post_rootfs_shell_commands() {
    # enable ctrlaltdel reboot because PV driver uses ctrl+alt+del to interpret reboot issued via xenstore
    echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab;

    # Trick to resolve dom0 name with argo.
    echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts;
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "
