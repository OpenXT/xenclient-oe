DESCRIPTION = "Linux kernel XenClient syncvm"
COMPATIBLE_MACHINE = "(xenclient-syncvm)"

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"
PV_MINOR = "${@"${PV}".split('.', 3)[1]}"

require linux-xenclient-${PV_MAJOR}.${PV_MINOR}.inc

PR = "1"

