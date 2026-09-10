DESCRIPTION = "Haskell bindings to libargo"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += " \
    hkg-network \
    libargo \
    libxchutils \
"
RDEPENDS:${PN} += "glibc-gconv-utf-32"

require xclibs.inc

S = "${UNPACKDIR}/${BP}/xchargo"

HPN = "xchargo"
HPV = "0.1"

require xclibs-haskell.inc
