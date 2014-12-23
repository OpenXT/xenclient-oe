SECTION = "base"
DESCRIPTION = "Package providing /etc/modules file"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r1"

SRC_URI = "file://modules"

do_install() {
	install -d ${D}${sysconfdir}
	install ${WORKDIR}/modules ${D}${sysconfdir}
}
