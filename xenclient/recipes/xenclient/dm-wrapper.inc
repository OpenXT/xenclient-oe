DESCRIPTION = "Wrapper which handles multiple device models"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "xen xen-tools libv4v libdmbus libevent"

SRC_URI = "${OPENXT_GIT_MIRROR}/dm-wrapper.git;protocol=git;tag=${OPENXT_TAG}"
S = "${WORKDIR}/git"

#EXTRA_OECONF += "--with-libxc=yes"
EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools
inherit xenclient
