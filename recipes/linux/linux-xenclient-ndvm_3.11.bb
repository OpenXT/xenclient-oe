DESCRIPTION = "Linux kernel XenClient ndvm"
COMPATIBLE_MACHINE = "(xenclient-ndvm)"

require linux-xenclient-${PV}.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-3.11"
