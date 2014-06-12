DESCRIPTION = "Device Model Bus Library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "libv4v"

SRC_URI = "${OPENXT_GIT_MIRROR}/xctools.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git/libdmbus"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient

