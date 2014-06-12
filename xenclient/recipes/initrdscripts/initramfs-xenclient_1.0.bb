LICENSE = "MIT"
LIC_FILES_CHKSUM="file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SRC_URI = "file://init.sh"
PR = "r2"
DESCRIPTON = "XenClient initramfs init script"

do_install() {
        install -m 0755 ${WORKDIR}/init.sh ${D}/init
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES_${PN} += " /init "
