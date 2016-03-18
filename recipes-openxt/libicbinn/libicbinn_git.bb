DESCRIPTION = "libicbinn"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/icbinn.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"
SRC_URI += "file://icbinn_svc.initscript"

DEPENDS = "libv4v libtirpc xenclient-idl xenclient-rpcgen-native libxcdbus"
EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR} --with-v4v --with-xcdbus"

PACKAGES =+ "${PN}-server"
FILES_${PN}-server = "${sysconfdir}/init.d ${bindir}/icbinn_svc"
PROVIDES += "${PN}-server"

PACKAGES =+ "${PN}-client"
FILES_${PN}-client = "${bindir}/icbinn_ftp"
PROVIDES += "${PN}-client"

S = "${WORKDIR}/git/libicbinn"

inherit autotools-brokensep pkgconfig lib_package xenclient

INITSCRIPT_NAME = "icbinn_svc"
INITSCRIPT_PARAMS = "defaults 76"
INITSCRIPT_PACKAGES = "${PN}-server"
inherit update-rc.d
do_install_append() {
	install -d ${D}/${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/icbinn_svc.initscript ${D}/${sysconfdir}/init.d/icbinn_svc
}
