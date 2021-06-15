DESCRIPTION = "XenClient Network Slave"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    carrier-detect \
    libxchutils \
    hkg-hsyslog \
    libxch-rpc \
    libxchxenstore \
    networkmanager \
    hkg-regex-posix \
    hkg-deepseq \
    hkg-text \
    hkg-mtl \
    hkg-network \
    rpc-autogen \
"
RDEPENDS_${PN} += " \
    glibc-gconv-utf-32 \
    iproute2 \
"

require network.inc

S = "${WORKDIR}/git/nws"

inherit haskell update-rc.d

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "network-slave"
INITSCRIPT_PARAMS_${PN} = "defaults 29 15"

FILES_${PN} += "/usr/bin/network-slave"

do_install_append() {
    install -m 0755 -d ${D}${sysconfdir}/network-daemon
    install -m 0755 ${S}/dnsmasq-template ${D}${sysconfdir}/network-daemon/dnsmasq-template
    install -m 0755 ${S}/dnsmasq-script-template ${D}${sysconfdir}/network-daemon/dnsmasq-script-template
    install -m 0755 ${S}/bridge-connection ${D}${sysconfdir}/network-daemon/bridge-connection
    install -m 0755 ${S}/slave-connection ${D}${sysconfdir}/network-daemon/slave-connection

    install -m 0755 -d ${D}${sysconfdir}/network-daemon/scripts
    install -m 0755 ${S}/nw_notify ${D}${sysconfdir}/network-daemon/scripts/nw_notify

    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/nws.initscript ${D}${sysconfdir}/init.d/network-slave
    
    install -d ${D}/${sysconfdir}/udev/rules.d
    install -D -m 0644 ${S}/nws-notify.rules ${D}/${sysconfdir}/udev/rules.d/81-nws-notify.rules

    install -d ${D}/${sysconfdir}/dnsmasq-config
}
