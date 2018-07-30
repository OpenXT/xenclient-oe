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

PR = "r15"

SRC_URI += " \
    file://network.ans \
    file://network_upgrade.ans \
    file://network_manual.ans \
    file://network_download_win.ans \
    file://network_manual_download_win.ans \
    file://pxelinux.cfg \
    file://isolinux.cfg \
    file://bootmsg.txt \
    file://installer-lvm.conf \
    file://grub.cfg \
"

IMAGE_FSTYPES = "cpio.gz"
export IMAGE_BASENAME = "xenclient-installer-image"

COMPATIBLE_MACHINE = "(openxt-installer)"

BAD_RECOMMENDATIONS += " \
    ${@bb.utils.contains('IMAGE_FEATURES', 'web-certificates', '', 'ca-certificates', d)} \
"

IMAGE_FEATURES += "empty-root-password"

IMAGE_INSTALL = "\
    initscripts \
    modules-installer \
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-xenclient-common \
    packagegroup-xenclient-installer \
    linux-firmware-iwlwifi \
    linux-firmware-bnx2 \
    linux-firmware-i915 \
"
# The entire installer rootfs is passed as the initramfs.
# Inflate the maximum value to 256M to reflect that (original definition is
# 128M in bitbake.conf)
INITRAMFS_MAXSIZE = "262144"

inherit image
inherit xenclient-licences

require xenclient-image-common.inc
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

    # Use bash as login shell
    sed -i 's|root:x:0:0:root:/root:/bin/sh|root:x:0:0:root:/root:/bin/bash|' ${IMAGE_ROOTFS}/config/etc/passwd;

    # Create file to identify this as the host installer filesystem
    touch ${IMAGE_ROOTFS}/etc/xenclient-host-installer;
}
ROOTFS_POSTPROCESS_COMMAND += "post_rootfs_shell_commands; "

# Remove initscripts pulled-in by dependencies or not required for operation.
remove_nonessential_initscripts() {
    remove_initscript "blktap"
    remove_initscript "sshd-v4v"
}
ROOTFS_POSTPROCESS_COMMAND += "remove_nonessential_initscripts; "

## Work-around:
## Disable lvmetad in the installer. There seem to be a race with eudev and
## lvmetad that reproduce consistently with NVMe:
## Fresh-install on an existing OpenXT installation will fail with:
##   Can't open /dev/<nvme-symlink> exclusively.  Mounted filesystem?
##
# packagegroup-xenclient-dom0 provides lvm2, so have lvmetad running as lvm2
# utilities try to use it and warn in its absence.
#activate_lvmetad_initscript() {
#    update-rc.d -r ${IMAGE_ROOTFS} lvm2-lvmetad defaults 06
#}
#ROOTFS_POSTPROCESS_COMMAND += "activate_lvmetad_initscript; "
write_installer_config_files() {
    # Overwrite the existing configuration.
    install -m 0644 ${WORKDIR}/installer-lvm.conf ${IMAGE_ROOTFS}${sysconfdir}/lvm/lvm.conf
}
ROOTFS_POSTPROCESS_COMMAND += "write_installer_config_files; "

# Copy syslinux modules and configuration files.
syslinux_install_files() {
    # Netboot.
    mkdir -p ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot
    cp -f ${IMAGE_ROOTFS}/${datadir}/syslinux/ldlinux.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
    cp -f ${IMAGE_ROOTFS}/${datadir}/syslinux/mboot.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
    cp -f ${IMAGE_ROOTFS}/${datadir}/syslinux/libcom32.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
    cp -f ${WORKDIR}/pxelinux.cfg ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/

    # Iso.
    mkdir -p ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso
    cp -f ${IMAGE_ROOTFS}/${datadir}/syslinux/ldlinux.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
    cp -f ${IMAGE_ROOTFS}/${datadir}/syslinux/mboot.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
    cp -f ${IMAGE_ROOTFS}/${datadir}/syslinux/libcom32.c32 ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
    cp -f ${IMAGE_ROOTFS}/${datadir}/syslinux/isolinux.bin ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
    cp -f ${WORKDIR}/bootmsg.txt ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
    cp -f ${WORKDIR}/isolinux.cfg ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
    cp -f ${WORKDIR}/grub.cfg ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/iso/
}
IMAGE_POSTPROCESS_COMMAND += "syslinux_install_files; "

# Install TBoot image and ACMs.
tboot_install_files() {
    cp -f ${IMAGE_ROOTFS}/boot/tboot.gz ${DEPLOY_DIR_IMAGE}/
    cp -f ${IMAGE_ROOTFS}/boot/GM45_GS45_PM45_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/gm45.acm
    cp -f ${IMAGE_ROOTFS}/boot/4th_gen_i5_i7_SINIT_75.BIN ${DEPLOY_DIR_IMAGE}/hsw.acm
    cp -f ${IMAGE_ROOTFS}/boot/i5_i7_DUAL_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/duali.acm
    cp -f ${IMAGE_ROOTFS}/boot/i7_QUAD_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/quadi.acm
    cp -f ${IMAGE_ROOTFS}/boot/Q35_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/q35.acm
    cp -f ${IMAGE_ROOTFS}/boot/Q45_Q43_SINIT_51.BIN ${DEPLOY_DIR_IMAGE}/q45q43.acm
    cp -f ${IMAGE_ROOTFS}/boot/Xeon-5600-3500-SINIT-v1.1.bin ${DEPLOY_DIR_IMAGE}/xeon56.acm
    cp -f ${IMAGE_ROOTFS}/boot/Xeon-E7-8800-4800-2800-SINIT-v1.1.bin ${DEPLOY_DIR_IMAGE}/xeone7.acm
    cp -f ${IMAGE_ROOTFS}/boot/3rd_gen_i5_i7_SINIT_67.BIN ${DEPLOY_DIR_IMAGE}/ivb_snb.acm
    cp -f ${IMAGE_ROOTFS}/boot/5th_gen_i5_i7_SINIT_79.BIN ${DEPLOY_DIR_IMAGE}/bdw.acm
    cp -f ${IMAGE_ROOTFS}/boot/6th_gen_i5_i7_SINIT_71.BIN ${DEPLOY_DIR_IMAGE}/skl.acm
    cp -f ${IMAGE_ROOTFS}/boot/7th_gen_i5_i7-SINIT_74.bin ${DEPLOY_DIR_IMAGE}/kbl.acm
    cp -f ${IMAGE_ROOTFS}/boot/8th_gen_i5_i7-SINIT_76.bin ${DEPLOY_DIR_IMAGE}/cfl.acm
    cp -f ${IMAGE_ROOTFS}/boot/license-SINIT-ACMs.txt ${DEPLOY_DIR_IMAGE}/license-SINIT-ACMs.txt
}
IMAGE_POSTPROCESS_COMMAND += "tboot_install_files; "

# Install Xen in the installer image.
# This is a legacy procedure as the installer does not require Xen to run,
# presumably this was done so that users would know immediately before
# installing that Xen cannot be run on the hardware.
xen_install() {
    cp -f ${IMAGE_ROOTFS}/boot/xen.gz ${DEPLOY_DIR_IMAGE}/
}
IMAGE_POSTPROCESS_COMMAND += "xen_install; "

# Install the microcode binary blob in the installer image.
microcode_install() {
    cp -f ${IMAGE_ROOTFS}/boot/microcode_intel.bin ${DEPLOY_DIR_IMAGE}/microcode_intel.bin
}
IMAGE_POSTPROCESS_COMMAND += "microcode_install; "

# Install Answerfiles used to configure the OpenXT installer.
openxt_install_answerfiles() {
    for i in ${WORKDIR}/*.ans ; do
        cp -f ${i} ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}/netboot/
    done
}
IMAGE_POSTPROCESS_COMMAND += "openxt_install_answerfiles; "

# Re-enable do_fetch/do_unpack to fetch image specific configuration files
# (see SRC_URI).
python () {
    d.delVarFlag("do_fetch", "noexec");
    d.delVarFlag("do_unpack", "noexec");
}
