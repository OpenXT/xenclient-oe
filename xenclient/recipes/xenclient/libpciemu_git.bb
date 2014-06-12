DESCRIPTION = "Library PCI emulation"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen xen-tools libevent"

SRC_URI = "${OPENXT_GIT_MIRROR}/libpciemu.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git"

EXTRA_OECONF += "--with-libxc=yes"
EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient
