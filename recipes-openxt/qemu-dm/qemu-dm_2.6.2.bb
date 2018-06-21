DESCRIPTION = "QEMU hosted virtual machine monitor."

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

require qemu-dm.inc

EXTRA_OECONF += " --audio-drv-list=alsa "

PR = "${INC_PR}.6"
