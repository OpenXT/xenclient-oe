DESCRIPTION = "XenClient xenmgr"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    xen \
    xen-tools \
    libxenmgr-core \
    libxchutils \
    libxchargo \
    libxchxenstore \
    libxchdb \
    libxch-rpc \
    hkg-json \
    hkg-hsyslog \
    hkg-regex-posix \
    hkg-network \
    hkg-attoparsec \
    hkg-zlib \
    hkg-parsec \
    hkg-deepseq \
    hkg-text \
    hkg-mtl \
    hkg-split \
    xenmgr-data \
    rpc-autogen \
"

require manager.inc

SRC_URI += " \
    file://xenmgr_dbus.conf \
    file://xenstore-init-extra \
    file://xenmgr.initscript \
"

S = "${WORKDIR}/git/xenmgr"

inherit haskell update-rc.d

do_install_append() {
    install -m 0755 ${S}/setup-ica-vm ${D}${bindir}/setup-ica-vm
    install -m 0755 -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${WORKDIR}/xenmgr_dbus.conf ${D}${sysconfdir}/dbus-1/system.d/
    install -m 0755 -d ${D}${datadir}/xenclient
    install -m 0755 ${WORKDIR}/xenstore-init-extra ${D}${datadir}/xenclient/
    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/xenmgr.initscript ${D}${sysconfdir}/init.d/xenmgr
    install -m 0755 -d ${D}${datadir}/xenmgr-1.0/templates
    install -m 0755 -d ${D}${datadir}/xenmgr-1.0/templates/default
    install -m 0644 ${S}/../templates/default/* ${D}${datadir}/xenmgr-1.0/templates/default/
}

RDEPENDS_${PN} += " \
    glibc-gconv-utf-32 \
    xenclient-eula \
    xenclient-caps \
    heimdallr \
    bash \
    openssl-bin \
"

INITSCRIPT_NAME = "xenmgr"
INITSCRIPT_PARAMS = "defaults 80 16"

FILES_${PN} += " \
    ${datadir}/xenmgr-1.0/templates/default/* \
    ${datadir}/xenclient \
    ${sysconfdir}/dbus-1/system.d/xenmgr_dbus.conf \
    ${sysconfdir}/init.d/xenmgr \
"

