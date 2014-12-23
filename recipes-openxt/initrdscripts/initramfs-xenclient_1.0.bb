LICENSE = "MIT"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI = "file://init.sh"
PR = "r2"
DESCRIPTON = "XenClient initramfs init script"

do_install() {
        install -m 0755 ${WORKDIR}/init.sh ${D}/init
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES_${PN} += " /init "
