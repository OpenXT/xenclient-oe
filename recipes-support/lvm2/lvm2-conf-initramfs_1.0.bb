SUMMARY = "LVM configuration for the initramfs image."
DESCRIPTION = "LVM configuration for the initramfs image. OpenXT's initramfs \
does not rely on udev."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://lvm.conf"

inherit allarch

do_install() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/lvm
    install -m 644 ${WORKDIR}/lvm.conf ${D}${sysconfdir}/lvm/lvm.conf
}

RPROVIDES_${PN} = "lvm2-conf"
RCONFLICTS_${PN} = "lvm2-conf"

CONFFILES_${PN} = " \
    ${sysconfdir}/lvm/ \
"
