require qemu-dm.inc

#only stubdom specific patches in here, common patches belong in qemu-dm.inc
SRC_URI += " \
            file://0024-Stubdom-support-for-ISO-media-changes-after-boot.patch;striplevel=1 \
            file://qemu-ifup-stubdom \
            "

EXTRA_OECONF += "--enable-debug --disable-strip --audio-drv-list=openxt"

do_install_append(){
    install -m 0755 -d ${D}${sysconfdir}/qemu
    install -m 0755 ${WORKDIR}/qemu-ifup-stubdom ${D}${sysconfdir}/qemu/qemu-ifup
}

PR = "${INC_PR}.5"

