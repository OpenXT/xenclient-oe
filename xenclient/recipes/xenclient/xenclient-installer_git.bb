DESCRIPTION = "XenClient Installer"
RDEPENDS_${PN} = "xenclient-eula xenclient-keyboard-list xenclient-repo-certs xenclient-caps"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

inherit xenclient

PV = "0+git${SRCPV}"

SRCREV = "e3705f28f7c5ce36ac5b369ca7d5e5fc80f1c19f"
SRC_URI = "git://github.com/openxt/installer.git;protocol=https"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-part2"

FILES_${PN} = "/install/*"
FILES_${PN}-part2 = "/*"

do_install () {
    ${S}/install part1 ${D}/install
    ${S}/install part2 ${D}
}
