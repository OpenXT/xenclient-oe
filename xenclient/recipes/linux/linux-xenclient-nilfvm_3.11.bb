DESCRIPTION = "Linux kernel XenClient nilfvm"
COMPATIBLE_MACHINE = "(xenclient-nilfvm)"

require linux-xenclient-${PV}.inc

inherit xenclient-deb-kernel

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEPENDS = " ${@deb_bootstrap_deps(d)} "

DEB_DESC = "${DESCRIPTION}"
DEB_DESC_EXT = "This package provides the linux kernel image for XenClient VPN domain."

