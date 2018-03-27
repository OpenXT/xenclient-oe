# XenClient Synchronizer client VM image

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

IMAGE_FEATURES += " \
    package-management \
"

IMAGE_FSTYPES = "ext3.vhd.gz"
export IMAGE_BASENAME = "xenclient-syncvm-image"

COMPATIBLE_MACHINE = "(xenclient-syncvm)"

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
    wget \
    sync-client \
    ifplugd \
    xenclient-syncvm-tweaks \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', 'xen-blktap', 'blktap3', d)} \
"

require xenclient-image-common.inc
require xenclient-version.inc
inherit xenclient-licences
inherit image

post_rootfs_shell_commands() {
    # enable ctrlaltdel reboot because PV driver uses ctrl+alt+del to interpret reboot issued via xenstore
    echo 'ca:12345:ctrlaltdel:/sbin/shutdown -t1 -a -r now' >> ${IMAGE_ROOTFS}/etc/inittab;

    # Change root shell.
    sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/etc/passwd;

    # Trick to resolve dom0 name with V4V.
    echo '1.0.0.0 dom0' >> ${IMAGE_ROOTFS}/etc/hosts;

    # TODO: This can be handled through populate-volatiles.sh
    # Create read-only rootfs required links.
    rm -f ${IMAGE_ROOTFS}/etc/resolv.conf;
    ln -s /var/volatile/etc/resolv.conf ${IMAGE_ROOTFS}/etc/resolv.conf;
    rm -f ${IMAGE_ROOTFS}/etc/network/interfaces;
    ln -s /var/volatile/etc/network/interfaces ${IMAGE_ROOTFS}/etc/network/interfaces;
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "

remove_nonessential_initscripts() {
    remove_initscript "urandom"
}
ROOTFS_POSTPROCESS_COMMAND += "remove_nonessential_initscripts; "
