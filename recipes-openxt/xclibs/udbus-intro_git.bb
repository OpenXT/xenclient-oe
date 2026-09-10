DESCRIPTION = "introspection XML parser for udbus"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "udbus hkg-haxml"
RDEPENDS:${PN} += "glibc-gconv-utf-32"

require xclibs.inc

S = "${UNPACKDIR}/${BP}/udbus-intro"

HPV = "0.1"
require xclibs-haskell.inc
