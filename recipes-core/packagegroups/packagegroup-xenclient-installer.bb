DESCRIPTION = "All packages required for XenClient installer"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PR = "r11"

inherit packagegroup

RDEPENDS_${PN} = " \
    acms \
    bc \
    cryptsetup \
    dialog \
    dmidecode \
    dosfstools \
    e2fsprogs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs \
    e2fsprogs-tune2fs \
    efibootmgr \
    eject \
    ethtool \
    gptfdisk \
    grub-efi \
    intel-microcode \
    kernel-modules \
    lvm2 \
    ncurses \
    netcat \
    openssh \
    openssh-sshd-tcp-init \
    openssl \
    openxt-keymanagement \
    openxt-measuredlaunch \
    parted \
    pciutils-ids \
    read-edid \
    refpolicy-mcs \
    rsyslog \
    rsyslog-conf-dom0 \
    shim \
    syslinux \
    syslinux-isolinux \
    syslinux-ldlinux \
    syslinux-mboot \
    syslinux-pxelinux \
    tboot \
    tboot-utils \
    tpm2-tss \
    tpm2-tools \
    tpm-tools \
    trousers \
    trousers-data \
    util-linux \
    util-linux-fdisk \
    util-linux-mount \
    util-linux-sfdisk \
    util-linux-umount \
    wget \
    xenclient-get-config-key \
    xenclient-installer \
    xenclient-installer-tweaks \
    xenclient-pcrdiff \
    xenclient-tpm-scripts \
    xen-tools-console \
    xen-efi \
    xen-tools-flask-tools \
    xen \
    xen-tools-libxenguest \
    xen-tools-libxenlight \
    xen-tools-libxenstat \
    xen-tools-libxlutil \
    xen-tools-xenstat \
    xen-tools-xl \
    blktap3 \
"
