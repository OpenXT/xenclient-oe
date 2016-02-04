DESCRIPTION = "XenClient Installer"
RDEPENDS_${PN} = "xenclient-eula xenclient-keyboard-list xenclient-repo-certs xenclient-caps"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

inherit xenclient

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/installer.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
          file://new-busybox-dialog.patch \
          file://change-dom0-size.patch \
          file://new-fdisk-tweaks.patch \
          "

S = "${WORKDIR}/git"

PACKAGES += "${PN}-part2"

FILES_${PN} = "/install/*"
FILES_${PN}-part2 = "/*"

do_install () {
    ${S}/install part1 ${D}/install
    ${S}/install part2 ${D}
    # base-files provides a run directory and we should not conflict
    mv -f ${D}/run ${D}/run.installer
}
