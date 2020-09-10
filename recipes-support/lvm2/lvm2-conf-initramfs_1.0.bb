SUMMARY = "LVM configuration for the initramfs image."
DESCRIPTION = "LVM configuration for the initramfs image. OpenXT's initramfs \
does not rely on udev."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://lvm.conf"

inherit allarch
# The lvm2 recipe in openembedded-core uses multilib_script and MULTILIB_SCRIPTS
# to handle installation of lvm.conf, and doing so here ensures that the file
# is packaged in a compatible way.
inherit multilib_script

MULTILIB_SCRIPTS = "${PN}:${sysconfdir}/lvm/lvm.conf"

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
