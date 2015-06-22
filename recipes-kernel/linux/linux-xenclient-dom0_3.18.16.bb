DESCRIPTION = "Linux kernel for dom0 in OpenXT"
COMPATIBLE_MACHINE = "(xenclient-dom0)"

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"
PV_MINOR = "${@"${PV}".split('.', 3)[1]}"
PV_MICRO = "${@"${PV}".split('.', 3)[2]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xenclient-${PV_MAJOR}.${PV_MINOR}:"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV_MAJOR}.${PV_MINOR}:"

require linux-xenclient-${PV_MAJOR}.${PV_MINOR}.inc

SRC_URI += " \
            file://pciback-restrictive-attr.patch;patch=1 \
            file://0002-Add-thorough-reset-interface-to-pciback-s-sysfs.patch;patch=1 \
            "

PR = "1"

