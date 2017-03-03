DESCRIPTION = "Resize daemon for the UIVM"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libx11 xen libxrandr libxrender"
RDEPENDS_${PN} = " libx11 xen-xenstore libxrandr libxrender"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/resized.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git"

inherit autotools xenclient pkgconfig

