DESCRIPTION = "XenClient DB upgrade utility"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    hkg-text \
    hkg-mtl \
    hkg-network \
    hkg-json \
    hkg-utf8-string \
    openssl \
"
RDEPENDS_${PN} += " \
    glibc-gconv-utf-32 \
"

PV = "0+git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/manager.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/upgrade-db"

HPV = "1.0"
inherit haskell
