DESCRIPTION = "Power Management Daemon for OpenXT"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "dbus xen-tools pciutils libxcdbus libxcxenstore udev libnl yajl"

require xctools.inc
SRC_URI += " \
    file://xcpmd.initscript \
"

CFLAGS:prepend = " -I${STAGING_INCDIR}/libnl3 "

CFLAGS:append = " -Wno-unused-parameter -Wno-deprecated-declarations "

S = "${UNPACKDIR}/${BP}/xcpmd"

ASNEEDED = ""

inherit autotools update-rc.d pkgconfig xc-rpcgen-c

INITSCRIPT_NAME = "xcpmd"
INITSCRIPT_PARAMS = "defaults 60 19"

do_install:append() {
# RJP TODO remove xenpmd from xenclient-tools clam bake
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${UNPACKDIR}/xcpmd.initscript ${D}${sysconfdir}/init.d/xcpmd
}
