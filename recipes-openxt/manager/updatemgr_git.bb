DESCRIPTION = "XenClient Update Manager"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libargo \
    libxch-rpc \
    libxchxenstore \
    libxchutils \
    xen \
    hkg-hsyslog \
    hkg-network \
    hkg-monadprompt \
    hkg-http \
    hkg-xenstore \
    hkg-parsec \
    hkg-deepseq \
    hkg-text \
    hkg-mtl \
    hkg-json \
    hkg-regex-posix \
    hkg-hinotify \
    hkg-lifted-base \
    hkg-monad-control \
    hkg-transformers-base \
    hkg-monad-loops \
    rpc-autogen \
"
RDEPENDS_${PN} += " \
    glibc-gconv-utf-32 \
    openssl-bin \
"

require manager.inc

SRC_URI += " \
    file://updatemgr.initscript \
"

S = "${WORKDIR}/git/updatemgr"

inherit update-rc.d haskell

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "updatemgr"
INITSCRIPT_PARAMS_${PN} = "defaults 80 20"

do_install_append() {
	install -m 0755 -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/updatemgr.initscript ${D}${sysconfdir}/init.d/updatemgr
}

