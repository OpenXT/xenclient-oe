DESCRIPTION = "Library PCI emulation"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen xen-tools libevent"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/libpciemu.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git"

EXTRA_OECONF += "--with-libxc=yes"
EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

CFLAGS_append = " -Wno-strict-aliasing "

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient
