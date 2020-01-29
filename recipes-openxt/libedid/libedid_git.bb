DESCRIPTION = "libedid"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = ""

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/libedid.git"

S = "${WORKDIR}/git"

inherit autotools-brokensep pkgconfig

FILES_${PN}-dev += "/usr/bin/libedid-config"

PR = "r1"

