DESCRIPTION = "libbudgetvhd"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"

SRC_URI = "${OPENXT_GIT_MIRROR}/gene3fs.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git/libbudgetvhd"

inherit autotools
inherit pkgconfig
inherit xenclient

BBCLASSEXTEND="native"
