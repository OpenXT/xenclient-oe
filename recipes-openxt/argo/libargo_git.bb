DESCRIPTION = "XenClient Argo library and interposer"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "argo-module-headers"

require argo.inc

S = "${WORKDIR}/git/libargo"

inherit autotools-brokensep pkgconfig lib_package

EXTRA_OECONF += "--with-pic"

RDEPENDS_${PN} += "argo-module"
