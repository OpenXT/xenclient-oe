DESCRIPTION = "This tool lets you simulate keyboard input and mouse activity, \
move and resize windows, etc. It does this using X11's XTEST extension and \
other Xlib functions. Additionally, you can search for windows and move, \
resize, hide, and modify window properties like the title. If your window \
manager supports it, you can use xdotool to switch desktops, move windows \
between desktops, and change the number of desktops."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=2f9cbf7e9401cec8a38666a08851ce6b"
DEPENDS = "libx11 libxtst"

SRC_URI = "http://semicomplete.googlecode.com/files/xdotool-${PV}.tar.gz"

S = "${WORKDIR}/xdotool-${PV}"

LDFLAGS += "-lX11"

do_install_prepend() {
  export PREFIX=/usr
}

inherit autotools-brokensep
