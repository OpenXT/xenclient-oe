DESCRIPTION = "Haskell RPC library (wrapper around dbus)"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " \
    udbus \
    libxchargo \
    libxchutils \
    hkg-hsyslog \
    hkg-transformers-base \
    hkg-monad-control \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32"

require xclibs.inc

S = "${WORKDIR}/git/xch-rpc"

HPN = "xch-rpc"
HPV = "0.1"

require xclibs-haskell.inc
