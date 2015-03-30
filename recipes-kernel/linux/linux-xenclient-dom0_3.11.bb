DESCRIPTION = "Linux kernel XenClient dom0"
COMPATIBLE_MACHINE = "(xenclient-dom0)"

require linux-xenclient-${PV}.inc

SRC_URI += " \
            file://pciback-restrictive-attr.patch;striplevel=1 \
            "

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-3.11"

