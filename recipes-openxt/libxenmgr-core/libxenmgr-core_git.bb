DESCRIPTION = "xenmgr core"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libxch-rpc \
    libxchdb \
    hkg-parsec \
    hkg-errors \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32"

PV = "0+git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/manager.git"

S = "${WORKDIR}/git/xenmgr-core"

HPN = "xenmgr-core"
HPV = "0.1"

require recipes-openxt/xclibs/xclibs.inc
inherit haskell
