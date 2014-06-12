DESCRIPTION = "Xen PV Backend Library"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen-tools xen"

SRC_URI = "${OPENXT_GIT_MIRROR}/libxenbackend.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient

PR = "r1"
