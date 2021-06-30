DESCRIPTION = "XenClient Network Daemon"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libxchutils \
    hkg-hsyslog \
    libxch-rpc \
    hkg-regex-posix \
    hkg-deepseq \
    hkg-text \
    hkg-mtl \
    hkg-network \
    libxchdb \
    libxchxenstore \
    rpc-autogen \
"

require network.inc

S = "${WORKDIR}/git/nwd"

inherit haskell update-rc.d

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "network-daemon"
INITSCRIPT_PARAMS_${PN} = "defaults 28 15"

FILES_${PN} += "/usr/bin/network-daemon"

do_install_append() {
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/nwd.initscript ${D}${sysconfdir}/init.d/network-daemon

    install -m 0755 -d ${D}${sysconfdir}/network-daemon
}
