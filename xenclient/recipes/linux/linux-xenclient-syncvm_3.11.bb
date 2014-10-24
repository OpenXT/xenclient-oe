DESCRIPTION = "Linux kernel XenClient syncvm"
COMPATIBLE_MACHINE = "(xenclient-syncvm)"

require linux-xenclient-${PV}.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-3.11"
