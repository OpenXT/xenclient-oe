DESCRIPTION = "Audio helper (Add stubdomain audio support)"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    libargo \
    alsa-lib \
"

require xctools.inc

SRC_URI += " \
    file://audio_helper_start \
"

S = "${UNPACKDIR}/${BP}/audio_helper"

inherit autotools
inherit pkgconfig

do_install(){
    install -d "${D}${libdir}/xen/bin"
    install -m 755 "${B}/src/audio_helper" "${D}${libdir}/xen/bin/audio_helper"
    install -m 755 "${UNPACKDIR}/audio_helper_start" "${D}${libdir}/xen/bin/audio_helper_start"
}

FILES:${PN} += " \
    ${libdir}/xen/bin/audio_helper \
    ${libdir}/xen/bin/audio_helper_start \
"
FILES:${PN}-dbg += " \
    ${libdir}/xen/bin/.debug \
"
RDEPENDS:${PN} += "dbd-tools"
