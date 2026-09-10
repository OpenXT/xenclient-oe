DESCRIPTION = "libicbinn-resolved"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM="file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

require icbinn.inc

DEPENDS = "libicbinn"

S = "${UNPACKDIR}/${BP}/libicbinn_resolved"

inherit autotools
inherit pkgconfig
inherit lib_package
