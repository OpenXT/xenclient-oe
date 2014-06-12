DESCRIPTION = "Citrix USB Daemon for XenClient"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libusb-compat xen-tools libv4v libxcdbus xenclient-idl xenclient-rpcgen-native libevent libxcxenstore"
RDEPENDS += "libxcxenstore"

SRC_URI = "${OPENXT_GIT_MIRROR}/xc-vusb-daemon.git;protocol=git;tag=${OPENXT_TAG} \
           file://xenclient-vusb.initscript \
           "

EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR}"
# workaround for broken configure.in
EXTRA_OECONF += "--with-libexpat=${STAGING_LIBDIR}"
EXTRA_OECONF += "--with-libxenstore=${STAGING_LIBDIR}"

S = "${WORKDIR}/git"

inherit autotools
inherit xenclient
inherit update-rc.d

INITSCRIPT_NAME = "xenclient-vusb-daemon"
INITSCRIPT_PARAMS = "defaults 60"

do_install_append (){
        install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/xenclient-vusb.initscript \
		${D}/etc/init.d/xenclient-vusb-daemon
}
