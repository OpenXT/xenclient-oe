DESCRIPTION = "All packages required for XenClient dom0"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit xenclient
inherit packagegroup

RDEPENDS_${PN} = " \
    openssh \
    openssh-sshd-tcp-init \
    util-linux-mount \
    util-linux-umount \
    xen-console \
    xen-hvmloader \
    xen-hypervisor \
    xen-efi \
    xen-flask-tools \
    xen-libxenctrl \
    xen-libxenguest \
    xen-libxenlight \
    xen-libxenstat \
    xen-libxlutil \
    xen-ocaml-libs \
    xen-xenstat \
    xen-xenstored \
    xen-xl \
    xen-xsm-policy \
    grub \
    shim \
    tboot \
    tboot-utils \
    e2fsprogs-tune2fs \
    e2fsprogs-resize2fs \
    kernel-modules \
    libv4v \
    libv4v-bin \
    libedid \
    lvm2 \
    bridge-utils \
    iptables \
    iproute2 \
    qemu-dm \
    seabios \
    ovmf \
    xcpmd \
    vbetool \
    xenclient-toolstack \
    xenclient-input-daemon \
    xenclient-dom0-tweaks \
    xenclient-splash-images \
    xenclient-config-access \
    xenclient-cryptdisks \
    cryptsetup \
    xenclient-get-config-key \
    xenclient-root-ro \
    alsa-utils-alsactl \
    alsa-utils-scripts \
    alsa-utils-alsamixer \
    xenclient-boot-sound \
    curl \
    trousers \
    trousers-data \
    tpm-tools \
    tpm-tools-sa \
    xenclient-tpm-setup \
    pciutils-ids \
    acms \
    read-edid \
    openssl \
    ntpdate \
    dd-buffered \
    vhd-scripts \
    secure-vm \
    xenclient-sec-scripts \
    pmtools \
    svirt-interpose \
    selinux-load \
    ethtool \
    intel-microcode \
    rsyslog \
    rsyslog-conf-dom0 \
    logrotate \
    qemu-wrappers \
    dialog \
    xenclient-udev-force-discreet-net-to-eth0 \
    xenclient-nwd \
    wget \
    xenclient-repo-certs \
    gobi-loader \
    usb-modeswitch \
    upgrade-db \
    rpc-proxy \
    dbd \
    xenclient-language-sync \
    atapi-pt-helper \
    audio-helper \
    qmp-helper \
    compleat \
    xec \
    dmidecode \
    netcat \
    libicbinn-server \
    screen \
    xenclient-pcrdiff \
    drm-surfman-plugin \
    eject \
    linux-input \
    iputils-ping \
    vusb-daemon \
    xenmgr-data \
    updatemgr \
    uid \
    surfman \
    linuxfb-surfman-plugin \
    xenmgr \
    xen-xenstore \
    tpm2-tss \
    tpm2-tools \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', 'xen-blktap xen-libblktapctl xen-libvhd', 'blktap3', d)} \
    pesign \
"

# OE upgrade - temporarly disabled:

# gconf-dbus \
# xserver-xorg \
# xf86-video-intel-xenclient-dom0 \
# xf86-video-vesa-xenclient-dom0 \
#
