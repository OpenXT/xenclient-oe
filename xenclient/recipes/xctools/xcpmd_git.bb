DESCRIPTION = "Power Management Daemon for XenClient"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "xenclient-idl dbus xen-tools pciutils libxcdbus libxenacpi xenclient-rpcgen-native libxcxenstore udev libnl"

PV = "0+git${SRCPV}"

SRCREV = "80d1955ecbfe803997b3b98f5363bc76dc510478"
SRC_URI = "git://github.com/openxt/xctools.git;protocol=https \
	   file://xcpmd.initscript \
"

EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR}"

S = "${WORKDIR}/git/xcpmd"

inherit autotools
inherit xenclient
inherit update-rc.d

INITSCRIPT_NAME = "xcpmd"
INITSCRIPT_PARAMS = "defaults 60"

do_install_append() {
# RJP TODO remove xenpmd from xenclient-tools clam bake
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/xcpmd.initscript ${D}${sysconfdir}/init.d/xcpmd
}
