DESCRIPTION = "Library VGA emulator"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen xen-tools"

PV = "0+git${SRCPV}"

SRCREV = "1161eeba1d4d8bb3ad09da0a2e42001472474ed1"
SRC_URI = "git://github.com/openxt/surfman.git;protocol=https"

S = "${WORKDIR}/git/libvgaemu"

EXTRA_OECONF += "--with-libxc=yes"
EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient
