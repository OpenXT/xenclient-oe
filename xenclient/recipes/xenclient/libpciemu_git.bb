DESCRIPTION = "Library PCI emulation"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen xen-tools libevent"

PV = "0+git${SRCPV}"

SRCREV = "f5faba787ae75bd83fdf32244c0df8a3aa7c7e73"
SRC_URI = "git://github.com/openxt/libpciemu.git;protocol=https"

S = "${WORKDIR}/git"

EXTRA_OECONF += "--with-libxc=yes"
EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient
