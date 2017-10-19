DESCRIPTION = "SeaBIOS"
HOMEPAGE = "http://www.coreboot.org/SeaBIOS"
LICENSE = "LGPLv3"
SECTION = "firmware"

SRC_URI = " \
    https://code.coreboot.org/p/seabios/downloads/get/${PN}-${PV}.tar.gz;name=tarball \
"

S = "${WORKDIR}/${PN}-${PV}"

inherit xenclient

