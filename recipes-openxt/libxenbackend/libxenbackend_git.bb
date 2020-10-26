DESCRIPTION = "Xen PV Backend Library"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen"

PV = "0+git${SRCPV}"

SRCREV = "66c02bd96475a63f7ae3ed3f1300c8f9dcd5031e"
SRC_URI = "git://github.com/OpenXT/libxenbackend.git"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools-brokensep pkgconfig lib_package
