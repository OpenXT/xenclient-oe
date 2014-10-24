DESCRIPTION = "pv audio backend"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libv4v speex libevent libxcxenstore libxenbackend alsa-lib "

PV = "0+git${SRCPV}"

SRCREV = "80d1955ecbfe803997b3b98f5363bc76dc510478"
SRC_URI = "git://github.com/openxt/xctools.git;protocol=https \
          file://audio-daemon-start"

FILES_${PN} += "/usr/lib/xen/bin/audio-daemon \
               /usr/lib/xen/bin/audio-daemon-start "

S = "${WORKDIR}/git/audio-daemon"

inherit autotools
inherit pkgconfig
inherit xenclient

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${S}/src/audio-daemon ${D}/usr/lib/xen/bin/
	install -m 755 ${WORKDIR}/audio-daemon-start ${D}/usr/lib/xen/bin/
}
