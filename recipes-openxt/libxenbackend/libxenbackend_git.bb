DESCRIPTION = "Xen PV Backend Library"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen-tools"

PV = "0+git${SRCPV}"

SRCREV = "6049292e6e3e1592bc8ab0eca3708d0b7fcd8a9c"
SRC_URI = "git://github.com/OpenXT/libxenbackend.git;protocol=https"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools-brokensep pkgconfig lib_package
