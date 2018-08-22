SRC_URI[md5sum] = "a9fe7efbc67a6966c4d4501f0cf88073"
SRC_URI[sha256sum] = "921a857dcf90a59df7feb26da5aabcf0bbfb749c46e2a46c3bc7a0280b83b0e1"
DESCRIPTION = "Common X11 Keyboard layouts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=42fb5e87e0d45120809ed5866524834c"
DEPENDS = "intltool-native xkbcomp-native glib-2.0 glib-2.0-native"

RDEPENDS_${PN} = "xkbcomp"
PR = "r4"

SRC_URI = "http://xorg.freedesktop.org/releases/individual/data/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://abnt2-fixes.patch;patch=1 \
           file://symlink-fix.patch;patch=1 \
           file://remove-generated-files.patch;patch=1 \
           file://drop-jp106-kr106-abnt2-models.patch;patch=1 \
           file://fix-merge-script-path.patch"

inherit autotools

#do_stage() {
#        autotools_stage_all
#}

do_install_append () {
    install -d ${D}/usr/share/X11/xkb/compiled
    cd ${D}${datadir}/X11/xkb/rules && ln -sf base xorg
}

do_configure_append () {
    # restore empty files the got mistakenly removed before release was packaged
    if [ ! -f ${S}/rules/base.o_k.part ]; then
        touch ${S}/rules/base.o_k.part
    fi
    if [ ! -f ${S}/rules/base.lo_s.part ]; then
        touch ${S}/rules/base.lo_s.part
    fi
}

FILES_${PN} += "${datadir}/X11/xkb"
