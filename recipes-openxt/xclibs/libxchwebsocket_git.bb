DESCRIPTION = "haskell websocket library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " \
    hkg-utf8-string \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32 hkg-utf8-string"

require xclibs.inc

S = "${WORKDIR}/git/xchwebsocket"

HPN = "xchwebsocket"
HPV = "0.1"

require xclibs-haskell.inc
