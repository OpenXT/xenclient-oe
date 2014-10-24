DESCRIPTION = "gene3fs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

DEPENDS += "libbudgetvhd"
DEPENDS += "e2fsprogs"

PV = "0+git${SRCPV}"

SRCREV = "52356fd1fe5af97e37213dbbc0e87f738db74b13"
SRC_URI = "git://github.com/openxt/gene3fs.git;protocol=https"

S = "${WORKDIR}/git/gene3fs"

inherit autotools
inherit pkgconfig
inherit xenclient

BBCLASSEXTEND="native"
