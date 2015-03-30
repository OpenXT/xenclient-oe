DESCRIPTION = "Plex86/Bochs LGPL VGABios"
HOMEPAGE = "http://www.nongnu.org/vgabios/"
LICENSE = "LGPLv2.1"
SECTION = "firmware"
DEPENDS = ""

SRC_URI = "http://savannah.gnu.org/download/vgabios/${PN}-${PV}.tgz;name=tarball"

S = "${WORKDIR}/${PN}-${PV}"

inherit xenclient
