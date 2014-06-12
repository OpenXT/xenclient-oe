DESCRIPTION = "UIVM Blanker for XenClient"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libx11 xen-tools libpciaccess"
RDEPENDS_${PN} = "libx11 xen-tools-xenstore-utils libpciaccess"

SRC_URI = "${OPENXT_GIT_MIRROR}/xblanker.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git"

inherit autotools
inherit xenclient

do_configure_prepend() {
#  cp ${WORKDIR}/xs.h ${S}/src/
}
