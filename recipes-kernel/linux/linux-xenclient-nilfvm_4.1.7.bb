DESCRIPTION = "Linux kernel XenClient nilfvm"
COMPATIBLE_MACHINE = "(xenclient-nilfvm)"

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"
PV_MINOR = "${@"${PV}".split('.', 3)[1]}"
PV_MICRO = "${@"${PV}".split('.', 3)[2]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xenclient-${PV_MAJOR}.${PV_MINOR}:"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV_MAJOR}.${PV_MINOR}:"

require linux-xenclient-${PV_MAJOR}.${PV_MINOR}.inc


inherit xenclient-deb-kernel

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEPENDS = " ${@deb_bootstrap_deps(d)} "

DEB_DESC = "${DESCRIPTION}"
DEB_DESC_EXT = "This package provides the linux kernel image for XenClient VPN domain."

