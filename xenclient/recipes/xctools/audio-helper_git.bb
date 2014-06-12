DESCRIPTION = "Audio helper (Add stubdomain audio support)"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libv4v alsa-lib "

SRC_URI = "${OPENXT_GIT_MIRROR}/xctools.git;protocol=git;tag=${OPENXT_TAG} \
          file://audio_helper_start"

FILES_${PN} += "/usr/lib/xen/bin/audio_helper \
               /usr/lib/xen/bin/audio_helper_start "

S = "${WORKDIR}/git/audio_helper"

inherit autotools
inherit pkgconfig
inherit xenclient

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${S}/src/audio_helper ${D}/usr/lib/xen/bin/
	install -m 755 ${WORKDIR}/audio_helper_start ${D}/usr/lib/xen/bin/
}
