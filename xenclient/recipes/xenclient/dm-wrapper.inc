DESCRIPTION = "Wrapper which handles multiple device models"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "xen xen-tools libv4v libdmbus libevent"

PV = "0+git${SRCPV}"

SRC_URI = "git://github.com/openxt/dm-wrapper.git;protocol=https"

S = "${WORKDIR}/git"

#EXTRA_OECONF += "--with-libxc=yes"
EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools
inherit xenclient
