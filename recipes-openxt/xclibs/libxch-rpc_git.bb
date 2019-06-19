DESCRIPTION = "Haskell RPC library (wrapper around dbus)"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " \
    udbus \
    libxchargo \
    hkg-network-bytestring \
    hkg-hsyslog \
    hkg-transformers-base \
    hkg-monad-control \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32"

PV = "0+git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xclibs.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/xch-rpc"

HPN = "xch-rpc"
HPV = "0.1"

require xclibs.inc
inherit haskell
