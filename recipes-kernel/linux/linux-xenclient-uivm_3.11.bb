DESCRIPTION = "Linux kernel XenClient uivm"
COMPATIBLE_MACHINE = "(xenclient-uivm)"

require linux-xenclient-${PV}.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-3.11"
