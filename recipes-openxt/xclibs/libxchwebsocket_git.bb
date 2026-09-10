DESCRIPTION = "haskell websocket library"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " \
    hkg-utf8-string \
"
RDEPENDS:${PN} += "glibc-gconv-utf-32 hkg-utf8-string"

require xclibs.inc

S = "${UNPACKDIR}/${BP}/xchwebsocket"

HPN = "xchwebsocket"
HPV = "0.1"

require xclibs-haskell.inc
