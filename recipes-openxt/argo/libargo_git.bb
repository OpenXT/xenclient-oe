DESCRIPTION = "XenClient Argo library and interposer"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "argo-module-headers"

require argo.inc

S = "${UNPACKDIR}/${BP}/libargo"

inherit autotools-brokensep pkgconfig lib_package

EXTRA_OECONF += "--with-pic"

RDEPENDS:${PN} += "argo-module"
