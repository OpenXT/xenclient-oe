DESCRIPTION = "libedid"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = ""

PV = "0+git${SRCPV}"

SRCREV = "bad8ca87e4649e9aa931c607e6a17a7881c2cfd6"
SRC_URI = "git://github.com/openxt/libedid.git;protocol=https"

S = "${WORKDIR}/git"

inherit autotools
inherit pkgconfig
inherit xenclient

FILES_${PN}-dev += "/usr/bin/libedid-config"

PR = "r1"

