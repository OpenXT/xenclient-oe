DESCRIPTION = "XenClient DBUS socket connections dom0-uivm"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
DEPENDS = "libv4v"
RDEPENDS += "xen-tools-libxenstore"

DEPENDS_append_xenclient-nilfvm += " ${@deb_bootstrap_deps(d)} "

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://dbusbouncer.c \
	   file://dbusbouncer.initscript \
"

INITSCRIPT_NAME = "dbusbouncer"
INITSCRIPT_PARAMS = "defaults 29"

S = "${WORKDIR}"

inherit update-rc.d xenclient
inherit ${@"xenclient-simple-deb"if(bb.data.getVar("MACHINE",d,1)=="xenclient-nilfvm")else("null")}

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_NAME = "xenclient-dbusbouncer"
DEB_DESC="V4V - UNIX socket proxy"
DEB_DESC_EXT="This package provides a proxy for remote socket access using V4V."
DEB_SECTION="misc"
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

LDFLAGS += "-lv4v -lxenstore"

do_compile() {
	oe_runmake dbusbouncer
	# ${STRIP} dbusbouncer
}

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/dbusbouncer ${D}${sbindir}
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/dbusbouncer.initscript ${D}${sysconfdir}/init.d/dbusbouncer
}

# Had to duplicate, can't _append as xenclient-deb overrides it
do_install_xenclient-nilfvm() {
        install -d ${D}${sbindir}
        install -m 0755 ${WORKDIR}/dbusbouncer ${D}${sbindir}
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/dbusbouncer.initscript ${D}${sysconfdir}/init.d/dbusbouncer

	${STRIP} ${D}${sbindir}/dbusbouncer

        ## to generate deb package
        do_simple_deb_package
}

DEBUG_BUILD = "1"
