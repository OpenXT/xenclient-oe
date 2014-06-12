DESCRIPTION = "Plugin for Surfman using libDRM"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "libsurfman udev libdrm"
INSANE_SKIP_${PN} = "dev-so"

SRC_URI = "${OPENXT_GIT_MIRROR}/surfman.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git/plugins/drm/"

PACKAGES = "${PN}-dev ${PN}-dbg ${PN}"
FILES_${PN}-dev += " /usr/lib/surfman/*.a /usr/lib/surfman/*.la "
FILES_${PN}-dbg += " /usr/lib/surfman/.debug/* "
FILES_${PN} += " /usr/lib/surfman/* "

inherit autotools
inherit pkgconfig
inherit xenclient
