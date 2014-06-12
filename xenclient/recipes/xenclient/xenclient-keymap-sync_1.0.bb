DESCRIPTION = "XenClient keymap sync daemon"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SRC_URI = "file://keymap-sync"
RDEPENDS = "xenclient-keyboard-list"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/keymap-sync ${D}${sbindir}
}
