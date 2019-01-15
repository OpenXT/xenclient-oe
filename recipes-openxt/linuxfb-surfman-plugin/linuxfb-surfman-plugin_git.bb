DESCRIPTION = "linuxfb-surfman-plugin"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "libsurfman"
INSANE_SKIP_${PN} = "dev-so"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/surfman.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
          "

S = "${WORKDIR}/git/plugins/linuxfb"

CFLAGS_append += " -Wno-unused-parameter "

FILES_${PN}-staticdev += " /usr/lib/surfman/*.a"
FILES_${PN}-dev += " /usr/lib/surfman/*.la "
FILES_${PN}-dbg += " /usr/lib/surfman/.debug/* "
FILES_${PN} += " /usr/lib/surfman/* "

inherit autotools
inherit pkgconfig
inherit xenclient
