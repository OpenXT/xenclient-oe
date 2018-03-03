DESCRIPTION = "XenClient toolstack"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "ocaml-dbus camomile xen xz"
RDEPENDS_${PN} = "xen-xenstore xen-xenstored"
RDEPENDS_${PN}_xenclient-ndvm += " db-tools"

DEPENDS_append_xenclient-nilfvm += " ${@deb_bootstrap_deps(d)} "

inherit autotools-brokensep ocaml findlib
inherit ${@"xenclient-simple-deb"if(d.getVar("MACHINE",1)=="xenclient-nilfvm")else("null")}

PACKAGES = "${PN}-dbg ${PN}-doc ${PN}-locale ${PN}-dev ${PN}-staticdev ${PN} \
            ${PN}-libs-dbg ${PN}-libs-staticdev ${PN}-libs-dev ${PN}-libs \
            "
# This is a little hybrid between usual package and findlib installation.
# findlib.bbclass redefines FILES, as ocaml packages are installed in
# ${sitelibdir} canonically.
FILES_${PN} = " \
    ${bindir} \
    ${sysconfdir} \
"
FILES_${PN}-dbg += " \
    /usr/src/debug \
"
FILES_${PN}-libs = " \
    ${sitelibdir}/*/*${SOLIBSDEV} \
"
FILES_${PN}-libs-dev = " \
    ${sitelibdir}/*/*.cm* \
    ${sitelibdir}/*/*.mli \
    ${sitelibdir}/*/META \
"
FILES_${PN}-libs-staticdev = " \
    ${sitelibdir}/*/*.a \
"
FILES_${PN}-libs-dbg = " \
    ${sitelibdir}/*/.debug \
"

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
           ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', 'file://0001-blktap3-move-physical-device-xenstore-node-creation-.patch', d)} \
           "

PACKAGE_ARCH = "${MACHINE_ARCH}"
S = "${WORKDIR}/git"

# TODO: ocamlc can figure it out in the build-system.
CFLAGS_append = " -I${ocamlincdir}"
do_compile() {
        make V=1 XEN_DIST_ROOT="${STAGING_DIR_HOST}"
}

OCAML_INSTALL_LIBS = " \
    libs/uuid \
    libs/stdext \
    libs/json \
    libs/jsonrpc \
    libs/http \
    libs/log \
    libs/common \
    "

do_configure_xenclient-nilfvm() {
        :
}

do_compile_xenclient-nilfvm() {
        :
}

do_install() {
        oe_runmake DESTDIR=${D} V=1 install
        rm -f ${D}/etc/xen/scripts/vif

        install -d ${D}/etc/xen/scripts
        install -m 0755 ${WORKDIR}/vif ${D}/etc/xen/scripts/vif

        for ocaml_lib in ${OCAML_INSTALL_LIBS}; do
            # Use of DESTDIR is not consistent here.
            # root Makefile sur $(DESTDIR)/usr/bin while libs use $(DESTDIR)/$(ocamlfind printconf destdir)
                oe_runmake -C $ocaml_lib V=1 install
        done
}

do_install_append_xenclient-nilfvm() {
	## to generate deb package
	DEB_DO_NOT_INCLUDE="usr/bin/ usr/lib/"
	do_simple_deb_package
}
