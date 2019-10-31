DESCRIPTION = "HID report descriptor I/O library and conversion tool"

inherit autotools pkgconfig lib_package

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=80e4bb872435a76f22ff66bc5542c74d"

DEPENDS += " libxml2 "

PV = "0+git${SRCPV}"

SRCREV = "82c5cdd05ffcfead8f48c89d4403f12f2f8cfd20"
SRC_URI = "git://github.com/DIGImend/${PN}.git;protocol=https;branch=master"

S = "${WORKDIR}/git/"

FILES_${PN} += " /usr/bin/hidrd-convert "
