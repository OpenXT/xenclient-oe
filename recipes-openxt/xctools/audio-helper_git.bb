DESCRIPTION = "Audio helper (Add stubdomain audio support)"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libargo \
    alsa-lib \
"

require xctools.inc

SRC_URI += " \
    file://audio_helper_start \
"

S = "${WORKDIR}/git/audio_helper"

inherit autotools
inherit pkgconfig

do_install(){
    install -d "${D}${libdir}/xen/bin"
    install -m 755 "${B}/src/audio_helper" "${D}${libdir}/xen/bin/audio_helper"
    install -m 755 "${WORKDIR}/audio_helper_start" "${D}${libdir}/xen/bin/audio_helper_start"
}

FILES_${PN} += " \
    ${libdir}/xen/bin/audio_helper \
    ${libdir}/xen/bin/audio_helper_start \
"
FILES_${PN}-dbg += " \
    ${libdir}/xen/bin/.debug \
"
RDEPENDS_${PN} += "dbd-tools"
