inherit module-compat
inherit xenclient

DESCRIPTION = "v4v kernel module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://v4v.h;beginline=6;endline=32;md5=8054a75b345d2cd08e16f9dd0ad9283b"

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_NAME = "v4v-module"
DEB_DESC="The XenClient v4v kernel module"
DEB_DESC_EXT="This package provides the XenClient v4v kernel module."
DEB_SECTION="misc"
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

DEPENDS_append += " v4v-module-headers"
DEPENDS_append_xenclient-nilfvm += " ${@deb_bootstrap_deps(d)} "

inherit ${@"xenclient-simple-deb"if(d.getVar("MACHINE",1)=="xenclient-nilfvm")else("null")}

PV = "git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/v4v.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
	   file://DEBIAN_postinst \
           "

S = "${WORKDIR}/git/v4v"

do_install_append_xenclient-nilfvm() {
	## to generate deb package
	sed -i "s|@KERNEL_VERSION@|${KERNEL_VERSION}|g" "${WORKDIR}/DEBIAN_postinst"
	do_simple_deb_package
}

MAKE_TARGETS += "modules"
