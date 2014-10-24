DESCRIPTION = "Device Model Bus Library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "libv4v"

PV = "0+git${SRCPV}"

SRCREV = "80d1955ecbfe803997b3b98f5363bc76dc510478"
SRC_URI = "git://github.com/openxt/xctools.git;protocol=https"

S = "${WORKDIR}/git/libdmbus"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient

