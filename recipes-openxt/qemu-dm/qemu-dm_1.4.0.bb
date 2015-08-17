DESCRIPTION = "QEMU hosted virtual machine monitor."

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"
PV_MINOR = "${@"${PV}".split('.', 3)[1]}"
PV_MICRO = "${@"${PV}".split('.', 3)[2]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV_MAJOR}.${PV_MINOR}"

require qemu-dm.inc

#Non-stubdom specific patches here, common patches belong in qemu-dm.inc
SRC_URI += " \
    file://0025-Enable-changing-of-ISO-media-in-non-stubdom-device-m.patch;striplevel=1 \
    "

EXTRA_OECONF += " --audio-drv-list=alsa "

PR = "${INC_PR}.4"
