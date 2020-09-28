DESCRIPTION = "Xen PV Backend Library"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/libxenbackend.git"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools-brokensep pkgconfig lib_package

PR = "r1"
