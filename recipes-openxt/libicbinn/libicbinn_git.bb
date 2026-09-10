DESCRIPTION = "libicbinn"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"

require icbinn.inc

SRC_URI += " \
    file://icbinn_svc.initscript \
    file://0001-canary-fix-icbinn_rand-prototype.patch \
    file://0002-client-include-ctype.h-for-isspace.patch \
"

DEPENDS = "libargo libtirpc libxcdbus"
EXTRA_OECONF += "--with-argo --with-xcdbus"
# server.c calls fallocate64(); glibc only declares it under _GNU_SOURCE.
# -fcommon: client/prototypes.h tentatively defines icb in every TU.
CFLAGS:append = " -D_GNU_SOURCE -fcommon"

PACKAGES =+ "${PN}-server"
FILES:${PN}-server = "${sysconfdir}/init.d ${bindir}/icbinn_svc"
PROVIDES += "${PN}-server"

PACKAGES =+ "${PN}-client"
FILES:${PN}-client = "${bindir}/icbinn_ftp"
PROVIDES += "${PN}-client"

S = "${UNPACKDIR}/${BP}/libicbinn"

inherit autotools-brokensep pkgconfig lib_package xc-rpcgen-c

INITSCRIPT_NAME = "icbinn_svc"
INITSCRIPT_PARAMS = "defaults 76 24"
INITSCRIPT_PACKAGES = "${PN}-server"
inherit update-rc.d
do_install:append() {
	install -d ${D}/${sysconfdir}/init.d
	install -m 0755 ${UNPACKDIR}/icbinn_svc.initscript ${D}/${sysconfdir}/init.d/icbinn_svc
}
