inherit module-compat
inherit xenclient

DESCRIPTION = "Linux Framebuffer PV driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

DEPENDS_append_xenclient-nilfvm += " ${@deb_bootstrap_deps(d)} "

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xenfb2.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
           "

S = "${WORKDIR}/git/linux"

inherit ${@"xenclient-simple-deb"if(d.getVar("MACHINE",1)=="xenclient-nilfvm")else("null")}

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_NAME = "xenfb2-kernel-module"
DEB_DESC="Linux Framebuffer PV driver"
DEB_DESC_EXT="This package provides a Linux PV framebuffer driver."
DEB_SECTION="misc"
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

MAKE_TARGETS += "modules"

FILES_${PN}-dev = " /usr/include "

do_install_headers() {
        install -m 0755 -d ${D}/usr/include
	install -m 644 fb2if.h ${D}/usr/include/fb2if.h
}

# Had to duplicate, can't _append as xenclient-deb overrides it
do_install_headers_xenclient-nilfvm() {
        install -m 0755 -d ${D}/usr/include
        install -m 644 fb2if.h ${D}/usr/include/fb2if.h

        ## to generate deb package
        do_simple_deb_package
}

addtask install_headers after do_install before do_package do_populate_sysroot
do_install_headers[dirs] = "${B}"
