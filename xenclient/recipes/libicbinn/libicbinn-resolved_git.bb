DESCRIPTION = "libicbinn-resolved"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM="file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

PV = "0+git${SRCPV}"

SRCREV = "4b4b14b9d3929413bab3a48992f4cfb03f1355d7"
SRC_URI = "git://github.com/openxt/icbinn.git;protocol=https"

DEPENDS = "libicbinn"

S = "${WORKDIR}/git/libicbinn_resolved"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient
