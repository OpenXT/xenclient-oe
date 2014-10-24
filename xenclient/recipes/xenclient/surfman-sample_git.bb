DESCRIPTION = "sample-plugin"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "libsurfman"

PV = "0+git${SRCPV}"

SRCREV = "1161eeba1d4d8bb3ad09da0a2e42001472474ed1"
SRC_URI = "git://github.com/openxt/surfman.git;protocol=https"

S = "${WORKDIR}/git/plugins/sample"

# Only compile this package to check that the sample plugin is still up to date

RDEPENDS_${PN}-dev = ""

FILES_${PN}-dev = "" 
FILES_${PN}-dbg = ""
FILES_${PN} = ""

inherit autotools
inherit pkgconfig
inherit xenclient

do_install() {
:
}
