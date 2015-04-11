DESCRIPTION = "Linux kernel XenClient dom0"
COMPATIBLE_MACHINE = "(xenclient-dom0)"

require linux-xenclient-${PV}.inc

SRC_URI += " \
            file://pciback-restrictive-attr.patch;striplevel=1 \
            file://0001-Backport-PCI-bus-and-slot-reset-functionality.patch;striplevel=1 \
            file://0002-Add-thorough-reset-interface-to-pciback-s-sysfs.patch;striplevel=1 \
            "

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-3.11"

PR="1"
