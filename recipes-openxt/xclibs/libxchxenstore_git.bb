DESCRIPTION = "Haskell bindings to xenstore"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += " \
    libxchutils \
    xen-tools \
    hkg-utf8-string \
"
RDEPENDS:${PN} += " \
    glibc-gconv-utf-32 \
    ghc-runtime \
"

require xclibs.inc

S = "${UNPACKDIR}/${BP}/xchxenstore"

HPN = "xchxenstore"
HPV = "0.1"

require xclibs-haskell.inc
