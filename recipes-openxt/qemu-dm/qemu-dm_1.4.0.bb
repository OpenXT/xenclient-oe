DESCRIPTION = "QEMU hosted virtual machine monitor."

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"
PV_MINOR = "${@"${PV}".split('.', 3)[1]}"
PV_MICRO = "${@"${PV}".split('.', 3)[2]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV_MAJOR}.${PV_MINOR}:"

require qemu-dm.inc

EXTRA_OECONF += " --audio-drv-list=alsa --enable-openxt-iso "

PR = "${INC_PR}.5"
