DESCRIPTION = "Haskell bindings to libargo"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += " \
    hkg-network \
    libargo \
    libxchutils \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32"

PV = "0+git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/xclibs.git"

S = "${WORKDIR}/git/xchargo"

HPN = "xchargo"
HPV = "0.1"

require xclibs.inc
inherit haskell
