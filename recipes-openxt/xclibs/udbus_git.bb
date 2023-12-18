DESCRIPTION = "haskell dbus library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=784a6790a51378ef1cc78d5c6999b241"
DEPENDS = " \
    hkg-cereal \
    hkg-network \
    hkg-utf8-string \
"
RDEPENDS_${PN} += "glibc-gconv-utf-32 hkg-utf8-string"

require xclibs.inc

S = "${WORKDIR}/git/udbus"

HPV = "0.2"
require xclibs-haskell.inc

FILES_${PN}-doc += "/usr/share/${PN}-${HPV}"
