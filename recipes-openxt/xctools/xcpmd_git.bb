DESCRIPTION = "Power Management Daemon for OpenXT"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "dbus xen-tools pciutils libxcdbus libxcxenstore udev libnl yajl"

require xctools.inc
SRC_URI += " \
    file://xcpmd.initscript \
"

CFLAGS_prepend += " -I${STAGING_INCDIR}/libnl3 "

CFLAGS_append += " -Wno-unused-parameter -Wno-deprecated-declarations "

S = "${WORKDIR}/git/xcpmd"

ASNEEDED = ""

inherit autotools update-rc.d pkgconfig xc-rpcgen-c

INITSCRIPT_NAME = "xcpmd"
INITSCRIPT_PARAMS = "defaults 60 40"

do_install_append() {
# RJP TODO remove xenpmd from xenclient-tools clam bake
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/xcpmd.initscript ${D}${sysconfdir}/init.d/xcpmd
}
