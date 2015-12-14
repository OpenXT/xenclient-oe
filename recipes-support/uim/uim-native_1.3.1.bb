require uim.inc
SRC_URI[md5sum] = "2832e23d4778bbacbfa4b49bf642d667"
SRC_URI[sha256sum] = "ed2cfa15018a4fd2557e875f66fcb3f0b9dabe12fa0700aa2f11cca69c2cb256"
DEPENDS = "gtk+ anthy intltool-native"
inherit native autotools-brokensep pkgconfig gettext
PR = "r2"

EXTRA_OECONF += "--disable-xim"

SRC_URI += "file://fix-missing-includes.patch;patch=1"
