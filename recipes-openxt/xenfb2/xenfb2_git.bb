inherit module-compat
inherit xenclient

DESCRIPTION = "Linux Framebuffer PV driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xenfb2.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
           "

S = "${WORKDIR}/git/linux"

MAKE_TARGETS += "modules"

FILES_${PN}-dev = " /usr/include "

do_install_headers() {
        install -m 0755 -d ${D}/usr/include
	install -m 644 fb2if.h ${D}/usr/include/fb2if.h
}

addtask install_headers after do_install before do_package do_populate_sysroot
do_install_headers[dirs] = "${B}"
