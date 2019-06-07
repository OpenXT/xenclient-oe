DESCRIPTION = "Audio helper (Add stubdomain audio support)"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " libargo alsa-lib "

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xctools.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
           file://audio_helper_start"

FILES_${PN} += "/usr/lib/xen/bin/audio_helper \
                /usr/lib/xen/bin/audio_helper_start "
FILES_${PN}-dbg += " /usr/lib/xen/bin/.debug "

S = "${WORKDIR}/git/audio_helper"

ASNEEDED = ""

inherit autotools
inherit pkgconfig
inherit xenclient

do_install(){
    install -d ${D}/usr/lib/xen/bin
    install -m 755 ${B}/src/audio_helper ${D}/usr/lib/xen/bin/
    install -m 755 ${WORKDIR}/audio_helper_start ${D}/usr/lib/xen/bin/
}
