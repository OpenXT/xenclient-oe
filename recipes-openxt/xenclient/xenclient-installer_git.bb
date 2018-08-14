DESCRIPTION = "XenClient Installer"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/installer.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
          "

S = "${WORKDIR}/git"

inherit xenclient allarch

PACKAGES += "${PN}-part2"

FILES_${PN} = "/install/*"
FILES_${PN}-part2 = "/*"

RDEPENDS_${PN} = " \
    busybox \
    xenclient-eula \
    xenclient-keyboard-list \
    xenclient-repo-certs \
    xenclient-caps \
"
RDEPENDS_${PN}-part2 += "busybox"

do_install () {
    ${S}/install part1 ${D}/install
    ${S}/install part2 ${D}
    # base-files provides a run directory and we should not conflict
    mv -f ${D}/run ${D}/run.installer
}
