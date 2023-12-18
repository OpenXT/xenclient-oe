DESCRIPTION = "xenmgr core"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libxch-rpc \
    libxchdb \
    hkg-errors \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32"

require manager.inc

S = "${WORKDIR}/git/xenmgr-core"

HPN = "xenmgr-core"
HPV = "0.1"

require recipes-openxt/xclibs/xclibs-haskell.inc
inherit haskell
