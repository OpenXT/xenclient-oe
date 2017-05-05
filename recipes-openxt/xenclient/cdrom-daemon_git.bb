DESCRIPTION = "CDROM Daemon for XenClient"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libxcdbus xenclient-idl xenclient-rpcgen-native xen-blktap"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/cdrom-daemon.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
           file://cdrom-daemon.initscript \
           "

EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR}"
EXTRA_OECONF += "--with-libxenstore=${STAGING_LIBDIR}"

S = "${WORKDIR}/git"

inherit autotools xenclient update-rc.d pkgconfig

INITSCRIPT_NAME = "cdrom-daemon"
INITSCRIPT_PARAMS = "defaults 60"

do_install_append (){
        install -d ${D}/etc/init.d
        install -m 0755 ${WORKDIR}/cdrom-daemon.initscript \
                ${D}/etc/init.d/cdrom-daemon
}
