DESCRIPTION = "This tool lets you simulate keyboard input and mouse activity, \
move and resize windows, etc. It does this using X11's XTEST extension and \
other Xlib functions. Additionally, you can search for windows and move, \
resize, hide, and modify window properties like the title. If your window \
manager supports it, you can use xdotool to switch desktops, move windows \
between desktops, and change the number of desktops."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=2f9cbf7e9401cec8a38666a08851ce6b"
DEPENDS = "virtual/libx11 libxtst"

inherit distro_features_check pkgconfig
# depends on virtual/libx11
REQUIRED_DISTRO_FEATURES = "x11"

SRC_URI = "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/semicomplete/xdotool-${PV}.tar.gz"

LDFLAGS += "-lX11"
EXTRA_OEMAKE = "PREFIX=${prefix} INSTALLLIB=${libdir} INSTALLMAN=${mandir}"

do_install() {
    oe_runmake -e install DESTDIR=${D} PREFIX=${prefix}
}
