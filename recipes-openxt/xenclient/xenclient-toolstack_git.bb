DESCRIPTION = "XenClient toolstack"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "ocaml-cross ocaml-dbus ocaml-camomile xen xz"
RDEPENDS_${PN} = "xen-xenstore xen-xenstored"
RDEPENDS_${PN}_xenclient-ndvm += " db-tools"

DEPENDS_append_xenclient-nilfvm += " ${@deb_bootstrap_deps(d)} "

inherit autotools-brokensep xenclient
inherit ${@"xenclient-simple-deb"if(d.getVar("MACHINE",1)=="xenclient-nilfvm")else("null")}

PACKAGES = "${PN}-dbg ${PN}-doc ${PN}-locale ${PN}-dev ${PN}-staticdev ${PN}"

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_NAME = "nilfvm-xenclient-toolstack"
DEB_DESC="The nilfvm XenClient toolstack package"
DEB_DESC_EXT="This package provides the  nilfvm XenClient toolstack scrips."
DEB_SECTION="misc"
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/toolstack.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}	\
           file://vif \
           "

PACKAGE_ARCH = "${MACHINE_ARCH}"
S = "${WORKDIR}/git"


do_compile() {
        :
}

do_configure_xenclient-nilfvm() {
        :
}

do_compile_xenclient-nilfvm() {
        :
}

do_install() {
        rm -f ${D}/etc/xen/scripts/vif

        install -d ${D}/etc/xen/scripts
        install -m 0755 ${WORKDIR}/vif ${D}/etc/xen/scripts/vif
}

do_install_append_xenclient-nilfvm() {
	## to generate deb package
	DEB_DO_NOT_INCLUDE="usr/bin/ usr/lib/"
	do_simple_deb_package
}

#do_stage() {
#	make V=1 STAGING_DIR="${STAGING_DIR}" STAGING_LIBDIR="${STAGING_LIBDIR}" stage
#}

# Avoid GNU_HASH check for the ocaml binaries
INSANE_SKIP_${PN} = "1"
