DESCRIPTION = "All packages required for XenClient dom0"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe      \
                    file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit xenclient
inherit task

RDEPENDS_${PN} = " \
    openssh \
    openssh-sshd-tcp-init \
    util-linux-mount \
    util-linux-umount \
    xen \
    xen-tools \
    xen-firmware \
    xen-xsm-policy \
    grub \
    tboot \
    e2fsprogs-tune2fs \
    kernel-modules \
    libv4v \
    libv4v-bin \
    libedid \
    libxenacpi \
    lvm2 \
    bridge-utils \
    iptables \
    iproute2 \
    ioemu \
    xcpmd \
    pmutil \
    vbetool-xc \
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
    alsa-utils-alsaconf \
    alsa-utils-alsamixer \
    xenclient-boot-sound \
    curl \
    trousers \
    trousers-data \
    tpm-tools \
    tpm-tools-sa \
    xenclient-tpm-setup \
    squashfs-tools \
    pciutils-ids \
    acms \
    read-edid \
    openssl \
    ntpdate \
    dd-buffered \
    vhd-copy \
    secure-vm \
    xenclient-sec-scripts \
    pmtools \
    xenaccess \
    blktap \
    svirt-interpose \
    selinux-load \
    ustr \
    ethtool \
    bootage \
    microcode-ctl \
    intel-microcode \
    rsyslog \
    logrotate \
    qemu-wrappers \
    dialog \
    xenclient-udev-force-discreet-net-to-eth0 \
    xenclient-nwd \
    wget \
    xen-tools-xenstored \
    xen-tools-xenconsoled \
    xenclient-repo-certs \
    gobi-loader \
    usb-modeswitch \
    upgrade-db \
    rpc-proxy \
    dbd \
    xenclient-language-sync \
    pci-dm-helper \
    atapi-pt-helper \
    audio-helper \
    compleat \
    xec \
    apptool \
    dmidecode \
    netcat \
    audio-daemon \
    linux-firmware \
    libicbinn-server \
    monit \
    upower \
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
    dm-agent \
    xenmgr \
"

# OE upgrade - temporarly disabled:

# gconf-dbus \
# xserver-xorg \
# xf86-video-intel-xenclient-dom0 \
# xf86-video-vesa-xenclient-dom0 \
#
