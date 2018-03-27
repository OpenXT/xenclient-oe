DESCRIPTION = "Haskell bindings to xenstore"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += " \
    libxchutils \
    xen \
    hkg-utf8-string \
"
RDEPENDS_${PN} += " \
    glibc-gconv-utf-32 \
    ghc-runtime \
"

PV = "0+git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xclibs.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/xchxenstore"

HPN = "xchxenstore"
HPV = "0.1"

require xclibs.inc
inherit haskell
