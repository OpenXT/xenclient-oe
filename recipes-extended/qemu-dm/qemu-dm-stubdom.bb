DESCRIPTION = "QEMU hosted virtual machine monitor for stub-domain."

FILESEXTRAPATHS_prepend := "${THISDIR}/qemu-dm:"

require qemu-dm.inc

EXTRA_OECONF += " --audio-drv-list=openxt --enable-openxt-stubdom "

PR = "${INC_PR}.9"
