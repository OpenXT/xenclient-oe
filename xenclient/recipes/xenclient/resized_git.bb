DESCRIPTION = "Resize daemon for the UIVM"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libx11 xen-tools libxrandr libxrender"
RDEPENDS_${PN} = " libx11 xen-tools-xenstore-utils libxrandr libxrender"

PV = "0+git${SRCPV}"

SRCREV = "10802fe68ff897bc2f0439e54eecce48266876e5"
SRC_URI = "git://github.com/openxt/resized.git;protocol=https"

S = "${WORKDIR}/git"

inherit autotools
inherit xenclient

