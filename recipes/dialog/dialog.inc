SECTION = "console/utils"
DEPENDS = "ncurses"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

DESCRIPTION = "Dialog lets you to present a variety of questions \
or display messages using dialog boxes from a shell \
script (or any scripting language)."

SRC_URI = "ftp://invisible-island.net/dialog/dialog-${PV}.tgz"

inherit autotools

do_configure() {
	oe_runconf
}
