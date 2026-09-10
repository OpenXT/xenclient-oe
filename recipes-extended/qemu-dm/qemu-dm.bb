DESCRIPTION = "QEMU hosted virtual machine monitor."

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

require qemu-dm.inc

SRC_URI += "\
    file://qemu-dm-wrapper \
"

EXTRA_OECONF += " --audio-drv-list=alsa "

PR = "${INC_PR}.6"

do_install:append() {
	install -m 0755 ${WORKDIR}/qemu-dm-wrapper ${D}${bindir}
}
