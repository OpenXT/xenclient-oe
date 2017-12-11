DESCRIPTION = "QEMU hosted virtual machine monitor for stub-domain."

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"
PV_MINOR = "${@"${PV}".split('.', 3)[1]}"
PV_MICRO = "${@"${PV}".split('.', 3)[2]}"

FILESEXTRAPATHS_prepend := "${THISDIR}/qemu-dm-${PV_MAJOR}.${PV_MINOR}.${PV_MICRO}:"

require qemu-dm.inc

# only stubdom specific patches in here, common patches belong in qemu-dm.inc
SRC_URI += " \
            file://qemu-ifup-stubdom \
            "

EXTRA_OECONF += " --audio-drv-list=openxt --enable-openxt-stubdom "

do_install_append(){
    install -m 0755 -d ${D}${sysconfdir}/qemu
    install -m 0755 ${WORKDIR}/qemu-ifup-stubdom ${D}${sysconfdir}/qemu/qemu-ifup
}

PR = "${INC_PR}.9"
