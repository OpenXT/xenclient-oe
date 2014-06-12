DESCRIPTION = "A window-matching utility, inspired by Sawfish's Matched Windows option"
LICENSE = "GPL"
SECTION = "x11"
PRIORITY = "optional"
DEPENDS = "glib-2.0 (>= 2.9.1) libwnck (>= 0.17) gtk+"
PR = "r0"

SRC_URI = "http://www.burtonini.com/computing/devilspie-0.22.tar.gz"

inherit autotools pkgconfig
