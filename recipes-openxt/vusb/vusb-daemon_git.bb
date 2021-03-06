DESCRIPTION = "Citrix USB Daemon for XenClient"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libusb-compat xen-tools libargo libxcdbus libevent libxcxenstore udev"
RDEPENDS_${PN} += "libxcxenstore"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/vusb-daemon.git \
           file://xenclient-vusb.initscript \
           "

# workaround for broken configure.in
EXTRA_OECONF += "--with-libexpat=${STAGING_LIBDIR}"
EXTRA_OECONF += "--with-libxenstore=${STAGING_LIBDIR}"

S = "${WORKDIR}/git"

inherit autotools update-rc.d pkgconfig xc-rpcgen-c

INITSCRIPT_NAME = "xenclient-vusb-daemon"
INITSCRIPT_PARAMS = "defaults 60 19"

do_install_append (){
        install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/xenclient-vusb.initscript \
		${D}/etc/init.d/xenclient-vusb-daemon
}
