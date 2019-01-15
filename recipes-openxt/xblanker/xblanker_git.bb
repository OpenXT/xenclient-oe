DESCRIPTION = "UIVM Blanker for XenClient"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libx11 xen libpciaccess"
RDEPENDS_${PN} = "libx11 xen-xenstore libpciaccess"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xblanker.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git"

inherit autotools xenclient pkgconfig

do_configure_prepend() {
#  cp ${WORKDIR}/xs.h ${S}/src/
}
