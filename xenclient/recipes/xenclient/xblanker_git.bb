DESCRIPTION = "UIVM Blanker for XenClient"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libx11 xen-tools libpciaccess"
RDEPENDS_${PN} = "libx11 xen-tools-xenstore-utils libpciaccess"

PV = "0+git${SRCPV}"

SRCREV = "80de9b1ed31b497e2c02b75a74237f7e92c53e05"
SRC_URI = "git://github.com/openxt/xblanker.git;protocol=https"

S = "${WORKDIR}/git"

inherit autotools
inherit xenclient

do_configure_prepend() {
#  cp ${WORKDIR}/xs.h ${S}/src/
}
