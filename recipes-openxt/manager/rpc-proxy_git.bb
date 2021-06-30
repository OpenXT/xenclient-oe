DESCRIPTION = "XenClient RPC proxy"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS += " \
    libxchutils \
    libxchwebsocket \
    libxchargo \
    libxchxenstore \
    libxch-rpc \
    libxchdb \
    hkg-dbus-core \
    hkg-json \
    hkg-hsyslog \
    hkg-network-bytestring \
    hkg-transformers \
    hkg-parsec \
    hkg-deepseq \
    hkg-text \
    hkg-mtl \
    hkg-network \
    hkg-monad-loops \
    hkg-lifted-base \
    hkg-monad-control \
    hkg-errors \
    rpc-autogen \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32 bash"

require manager.inc

SRC_URI += " \
    file://rpc-proxy.rules \
    file://rpc-proxy.initscript \
"

S = "${WORKDIR}/git/rpc-proxy"

HPV = "1.0"
require recipes-openxt/xclibs/xclibs-haskell.inc
inherit update-rc.d haskell

INITSCRIPT_NAME = "rpc-proxy"
INITSCRIPT_PARAMS = "defaults 30"

do_install_append() {
	install -m 0755 -d ${D}/etc
	install -m 0755 -d ${D}/etc/init.d
	install -m 0644 ${WORKDIR}/rpc-proxy.rules ${D}/etc/rpc-proxy.rules
	install -m 0755 ${WORKDIR}/rpc-proxy.initscript ${D}${sysconfdir}/init.d/rpc-proxy
}
