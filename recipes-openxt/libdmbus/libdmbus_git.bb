DESCRIPTION = "Device Model Bus Library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " \
    coreutils-native \
    libargo \
"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/xctools.git"

S = "${WORKDIR}/git/libdmbus"

ASNEEDED = ""

inherit autotools-brokensep pkgconfig lib_package

