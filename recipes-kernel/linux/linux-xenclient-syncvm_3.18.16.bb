DESCRIPTION = "Linux kernel XenClient syncvm"
COMPATIBLE_MACHINE = "(xenclient-syncvm)"

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"
PV_MINOR = "${@"${PV}".split('.', 3)[1]}"
PV_MICRO = "${@"${PV}".split('.', 3)[2]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xenclient-${PV_MAJOR}.${PV_MINOR}:"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV_MAJOR}.${PV_MINOR}:"

require linux-xenclient-${PV_MAJOR}.${PV_MINOR}.inc

PR = "1"

