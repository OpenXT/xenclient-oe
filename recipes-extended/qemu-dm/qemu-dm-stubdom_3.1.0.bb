DESCRIPTION = "QEMU hosted virtual machine monitor for stub-domain."

FILESEXTRAPATHS_prepend := "${THISDIR}/qemu-dm:"

require qemu-dm.inc

# only stubdom specific patches in here, common patches belong in qemu-dm.inc
SRC_URI += " \
            file://qemu-ifup-stubdom \
            "

EXTRA_OECONF += " --audio-drv-list=openxt --enable-openxt-stubdom "

do_install_append(){
    install -m 0755 -d ${D}${sysconfdir}/qemu
    install -m 0755 ${WORKDIR}/qemu-ifup-stubdom ${D}${sysconfdir}/qemu-ifup
}

PR = "${INC_PR}.9"
