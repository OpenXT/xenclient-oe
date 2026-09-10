DESCRIPTION = "haskell dbus library"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=784a6790a51378ef1cc78d5c6999b241"
DEPENDS = " \
    hkg-cereal \
    hkg-network \
    hkg-utf8-string \
"
RDEPENDS:${PN} += "glibc-gconv-utf-32 hkg-utf8-string"

require xclibs.inc

S = "${UNPACKDIR}/${BP}/udbus"

HPV = "0.2"
require xclibs-haskell.inc

FILES:${PN}-doc += "/usr/share/${PN}-${HPV}"
