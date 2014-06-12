DESCRIPTION = "XenClient Installer"
RDEPENDS_${PN} = "xenclient-eula xenclient-keyboard-list xenclient-repo-certs xenclient-caps"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

inherit xenclient

SRC_URI = "${OPENXT_GIT_MIRROR}/installer.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-part2"

FILES_${PN} = "/install/*"
FILES_${PN}-part2 = "/*"

do_install () {
    ${S}/install part1 ${D}/install
    ${S}/install part2 ${D}
}
