DESCRIPTION = "XenClient keymap sync daemon"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI = "file://keymap-sync"
RDEPENDS_${PN} = "xenclient-keyboard-list"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/keymap-sync ${D}${sbindir}
}
