DESCRIPTION = "xenmgr core"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libxch-rpc \
    libxchdb \
    hkg-errors \
"
RDEPENDS:${PN} += "glibc-gconv-utf-32"

require manager.inc

S = "${UNPACKDIR}/${BP}/xenmgr-core"

HPN = "xenmgr-core"
HPV = "0.1"

require recipes-openxt/xclibs/xclibs-haskell.inc
inherit haskell
