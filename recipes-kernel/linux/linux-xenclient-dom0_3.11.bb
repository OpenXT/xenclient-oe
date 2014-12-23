DESCRIPTION = "Linux kernel XenClient dom0"
COMPATIBLE_MACHINE = "(xenclient-dom0)"

require linux-xenclient-${PV}.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-3.11"

