DESCRIPTION = "Linux kernel XenClient stubdomain"
COMPATIBLE_MACHINE = "(xenclient-stubdomain)"

require linux-xenclient-${PV}.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-3.11"
