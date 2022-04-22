# Part 1 of the XenClient host installer.
#
# This is responsible for retrieving the XenClient repository and extracting
# and running part 2 of the host installer, which contains the logic to install
# or upgrade a specific version of XenClient.

LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

PR = "r16"

IMAGE_FSTYPES = "cpio.gz"
export IMAGE_BASENAME = "xenclient-installer-image"

COMPATIBLE_MACHINE = "(openxt-installer)"

BAD_RECOMMENDATIONS += " \
    ${@bb.utils.contains('IMAGE_FEATURES', 'web-certificates', '', 'ca-certificates', d)} \
"

INITSCRIPT_REMOVE = " \
    blktap \
    sshd-argo \
"

IMAGE_FEATURES += " \
    empty-root-password \
    root-bash-shell \
"

IMAGE_INSTALL = "\
    initscripts \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    packagegroup-xenclient-installer \
    linux-firmware-bnx2 \
    linux-firmware-i915 \
    linux-firmware-nvidia \
    linux-firmware-radeon \
    libdrm-nouveau \
    libdrm-radeon \
"
# The entire installer rootfs is passed as the initramfs.
# Inflate the maximum value to 512M to reflect that (original definition is
# 128M in bitbake.conf)
INITRAMFS_MAXSIZE = "524288"

inherit openxt-image
inherit xenclient-licences

require xenclient-version.inc

post_rootfs_shell_commands() {
    # Create /init symlink
    ln -s sbin/init ${IMAGE_ROOTFS}/init;

    # Update /etc/inittab
    sed -i '/^1:/d' ${IMAGE_ROOTFS}/etc/inittab;
    {
        echo '1:2345:once:/install/part1/autostart-main < /dev/tty1 > /dev/tty1';
        echo '2:2345:respawn:/usr/bin/tail -F /var/log/installer > /dev/tty2';
        echo '3:2345:respawn:/sbin/getty 38400 tty3';
        echo '4:2345:respawn:/usr/bin/tail -F /var/log/messages > /dev/tty4';
        echo '5:2345:respawn:/sbin/getty 38400 tty5';
        echo '6:2345:respawn:/sbin/getty 38400 tty6';
        echo '7:2345:respawn:/install/part1/autostart-status < /dev/tty7 > /dev/tty7';
        echo 'ca::ctrlaltdel:/sbin/reboot';
        echo 'S0:12345:respawn:/sbin/getty 115200 ttyS0';
    } >> ${IMAGE_ROOTFS}/etc/inittab;

    # Update /etc/network/interfaces
    {
        echo 'auto lo';
        echo 'iface lo inet loopback';
    } > ${IMAGE_ROOTFS}/etc/network/interfaces;

    # Password files are expected in /config
    mkdir -p ${IMAGE_ROOTFS}/config/etc;
    mv ${IMAGE_ROOTFS}/etc/shadow ${IMAGE_ROOTFS}/config/etc/shadow;
    mv ${IMAGE_ROOTFS}/etc/passwd ${IMAGE_ROOTFS}/config/etc/passwd;
    ln -s /config/etc/shadow ${IMAGE_ROOTFS}/etc/shadow;
    ln -s /config/etc/passwd ${IMAGE_ROOTFS}/etc/passwd;

    # Create file to identify this as the host installer filesystem
    touch ${IMAGE_ROOTFS}/etc/xenclient-host-installer;
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "
ROOTFS_POSTPROCESS_COMMAND += "start_tty_on_hvc0; "

# Install Xen in the installer image.
# This is a legacy procedure as the installer does not require Xen to run,
# presumably this was done so that users would know immediately before
# installing that Xen cannot be run on the hardware.
xen_install() {
    cp -f ${IMAGE_ROOTFS}/boot/xen.gz ${DEPLOY_DIR_IMAGE}/
}
IMAGE_POSTPROCESS_COMMAND += "xen_install; "
