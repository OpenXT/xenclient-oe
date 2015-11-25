DESCRIPTION = "X protocol headers: ${XORG_PN}"
HOMEPAGE = "http://www.x.org"
SECTION = "x11/libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=b9f79c119df756aeffcb89ec96716a9e"

DEPENDS = "util-macros"

XORG_PN = "${BPN}"

ALLOW_EMPTY_${PN} = "1"

SRC_URI = "${XORG_MIRROR}/individual/proto/${XORG_PN}-${PV}.tar.bz2"

S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

#do_stage() {
#        autotools_stage_all
#}
