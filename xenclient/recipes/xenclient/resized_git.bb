DESCRIPTION = "Resize daemon for the UIVM"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libx11 xen-tools libxrandr libxrender"
RDEPENDS_${PN} = " libx11 xen-tools-xenstore-utils libxrandr libxrender"

SRC_URI = "${OPENXT_GIT_MIRROR}/resized.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git"

inherit autotools
inherit xenclient

