DESCRIPTION = "haskell dbus library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=784a6790a51378ef1cc78d5c6999b241"
DEPENDS = " \
    hkg-binary \
    hkg-cereal \
    hkg-mtl \
    hkg-network \
    hkg-utf8-string \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32 hkg-utf8-string"

PV = "0+git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xclibs.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/udbus"

HPV = "0.2"
require xclibs.inc
inherit haskell

FILES_${PN}-doc += "/usr/share/${PN}-${HPV}"
