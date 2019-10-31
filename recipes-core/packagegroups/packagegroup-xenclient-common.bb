DESCRIPTION = "Common packages for XenClient images"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6      \
                    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

# depend on shadow-native, to make sure that pwconv is found during
# rootfs construction as shadow for various reasons does not depend on it
# and other packages may or may not pull it
# when removing shadow dependecy please remove also shadow-native
DEPENDS += "shadow-native"

RDEPENDS_${PN} = " \
    xenclient-feed-configs \
    shadow \
    bash \
    bzip2 \
    coreutils \
    gzip \
    ldd \
    less \
    procps \
    rsync \
    strace \
    vim-tiny \
    sysvinit-pidof \
    nano \
"
