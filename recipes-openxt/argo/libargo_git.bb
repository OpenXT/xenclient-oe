DESCRIPTION = "XenClient Argo library and interposer"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "argo-module-headers"

require argo.inc

S = "${UNPACKDIR}/${BP}/libargo"

inherit autotools-brokensep pkgconfig lib_package

EXTRA_OECONF += "--with-pic"
# ttcp.c still uses K&R empty prototypes; GCC 15/C23 treats foo() as foo(void).
CFLAGS:append = " -std=gnu89"

RDEPENDS:${PN} += "argo-module"
