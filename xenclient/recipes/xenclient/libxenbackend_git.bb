DESCRIPTION = "Xen PV Backend Library"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen-tools xen"

PV = "0+git${SRCPV}"

SRCREV = "62e57c7f656b0d3509983e68eb7f27fffc48d793"
SRC_URI = "git://github.com/openxt/libxenbackend.git;protocol=https"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient

PR = "r1"
