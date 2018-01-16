DESCRIPTION = "db tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "ocaml-dbus xenclient-toolstack xen-ocaml-libs"

DEPENDS_append_xenclient-nilfvm += " ${@deb_bootstrap_deps(d)} "

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/manager.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

SRC_URI += " file://db-exists-dom0 \
	     file://db-ls-dom0 \
	     file://db-nodes-dom0 \
	     file://db-read-dom0 \
	     file://db-rm-dom0 \
	     file://db-write-dom0 \
	     file://db-cat-dom0 "

S = "${WORKDIR}/git/dbd"

inherit xenclient ocaml findlib xc-rpcgen
inherit ${@"xenclient-simple-deb"if(d.getVar("MACHINE",1)=="xenclient-nilfvm")else("null")}

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_NAME = "xenclient-dbtools"
DEB_DESC="Tools to access the XenClient database from a service VM"
DEB_DESC_EXT="This package provides a set of tools to access the XenClient database from a service VM."
DEB_SECTION="misc"
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

do_configure_append() {
	mkdir -p ${S}/autogen
	xc-rpcgen --camel --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o ${S}/autogen ${STAGING_IDLDATADIR}/db.xml
}

do_compile() {
	# hack
	touch ${STAGING_LIBDIR}/ocaml/ld.conf
	make V=1 XEN_DIST_ROOT="${STAGING_DIR}" TARGET_PREFIX="${TARGET_PREFIX}" STAGING_DIR="${STAGING_DIR}" STAGING_BINDIR_CROSS="${STAGING_BINDIR_CROSS}" STAGING_LIBDIR="${STAGING_LIBDIR}" STAGING_INCDIR="${STAGING_INCDIR}" db-cmd
}

do_install() {
	# findlib.bbclass will create ${D}${sitelibdir} for generic ocamlfind
	# compliance with bitbake. This does not ship any library though.
	rm -rf ${D}${libdir}

	install -m 0755 -d ${D}/usr/bin

        install -m 0755 db-cmd ${D}/usr/bin/db-cmd

        install -m 0755 db-exists ${D}/usr/bin/db-exists
        install -m 0755 db-ls ${D}/usr/bin/db-ls
        install -m 0755 db-nodes ${D}/usr/bin/db-nodes
        install -m 0755 db-read ${D}/usr/bin/db-read
        install -m 0755 db-rm ${D}/usr/bin/db-rm
        install -m 0755 db-write ${D}/usr/bin/db-write
        install -m 0755 db-write ${D}/usr/bin/db-cat

        install -m 0755 ${WORKDIR}/db-exists-dom0 ${D}/usr/bin/db-exists-dom0
        install -m 0755 ${WORKDIR}/db-ls-dom0 ${D}/usr/bin/db-ls-dom0
        install -m 0755 ${WORKDIR}/db-nodes-dom0 ${D}/usr/bin/db-nodes-dom0
        install -m 0755 ${WORKDIR}/db-read-dom0 ${D}/usr/bin/db-read-dom0
        install -m 0755 ${WORKDIR}/db-rm-dom0 ${D}/usr/bin/db-rm-dom0
        install -m 0755 ${WORKDIR}/db-write-dom0 ${D}/usr/bin/db-write-dom0
        install -m 0755 ${WORKDIR}/db-write-dom0 ${D}/usr/bin/db-cat-dom0
}

# Had to duplicate, can't _append as xenclient-deb overrides it
do_install_xenclient-nilfvm() {
	# findlib.bbclass will create ${D}${sitelibdir} for generic ocamlfind
	# compliance with bitbake. This does not ship any library though.
	rm -rf ${D}${libdir}

        install -m 0755 -d ${D}/usr/bin

        install -m 0755 db-cmd ${D}/usr/bin/db-cmd

        install -m 0755 db-exists ${D}/usr/bin/db-exists
        install -m 0755 db-ls ${D}/usr/bin/db-ls
        install -m 0755 db-nodes ${D}/usr/bin/db-nodes
        install -m 0755 db-read ${D}/usr/bin/db-read
        install -m 0755 db-rm ${D}/usr/bin/db-rm
        install -m 0755 db-write ${D}/usr/bin/db-write
        install -m 0755 db-write ${D}/usr/bin/db-cat

        install -m 0755 ${WORKDIR}/db-exists-dom0 ${D}/usr/bin/db-exists-dom0
        install -m 0755 ${WORKDIR}/db-ls-dom0 ${D}/usr/bin/db-ls-dom0
        install -m 0755 ${WORKDIR}/db-nodes-dom0 ${D}/usr/bin/db-nodes-dom0
        install -m 0755 ${WORKDIR}/db-read-dom0 ${D}/usr/bin/db-read-dom0
        install -m 0755 ${WORKDIR}/db-rm-dom0 ${D}/usr/bin/db-rm-dom0
        install -m 0755 ${WORKDIR}/db-write-dom0 ${D}/usr/bin/db-write-dom0
        install -m 0755 ${WORKDIR}/db-write-dom0 ${D}/usr/bin/db-cat-dom0

        ## to generate deb package
        do_simple_deb_package
}

