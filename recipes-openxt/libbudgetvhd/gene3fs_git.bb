DESCRIPTION = "gene3fs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

DEPENDS += "libbudgetvhd"
DEPENDS += "e2fsprogs"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/gene3fs.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/gene3fs"

inherit autotools
inherit pkgconfig

BBCLASSEXTEND="native"
