DESCRIPTION = "HID report descriptor I/O library and conversion tool"

inherit xenclient
inherit autotools pkgconfig lib_package

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=80e4bb872435a76f22ff66bc5542c74d"

DEPENDS += " libxml2 "

SRC_URI = "https://github.com/DIGImend/${PN}/releases/download/${PV}/${PN}-${PV}.tar.gz;name=tarball"

SRC_URI[tarball.md5sum] = "6969c10103cda116681700764c1be90f"
SRC_URI[tarball.sha256sum] = "060c3f2ed20f6071440c8f8bfa7a063aa9529293f1a049ede76721ac3dcc7e95"

S = "${WORKDIR}/${PN}-${PV}/"

LDFLAGS += " -Wl,-rpath,../lib/item/.libs/ -Wl,-rpath,../lib/usage/.libs/ "

FILES_${PN} += " /usr/bin/hidrd-convert "

do_configure() {
    ./configure --prefix="/usr"
}
