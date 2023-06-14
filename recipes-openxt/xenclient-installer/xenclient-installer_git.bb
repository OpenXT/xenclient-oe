DESCRIPTION = "XenClient Installer"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0+git${SRCPV}"

SRCREV = "e20749637d9a8016af6721d4bc3968cd5978c60a"
SRC_URI = " \
    git://github.com/OpenXT/installer.git;protocol=https \
    file://network.ans \
    file://network_download_win.ans \
    file://network_manual.ans \
    file://network_manual_download_win.ans \
    file://network_upgrade.ans \
"

S = "${WORKDIR}/git"

inherit allarch deploy

do_install () {
    ${S}/install part1 ${D}/install
    ${S}/install part2 ${D}
    for f in \
        network.ans \
        network_download_win.ans \
        network_manual.ans \
        network_manual_download_win.ans \
        network_upgrade.ans
    do
        install -m 0644 "${WORKDIR}/${f}" "${D}/${f}"
    done
}

do_deploy() {
    install -m 0755 -d "${DEPLOYDIR}/netboot"
    install -m 0644 ${D}/*.ans ${DEPLOYDIR}/netboot/

    tar --exclude=./install \
        --owner=root --group=root \
        -C ${D} -cjf ${DEPLOYDIR}/control.tar.bz2 .
}
addtask do_deploy after do_install before do_build

PACKAGES += " \
    ${PN}-answerfiles \
    ${PN}-part2 \
"

FILES_${PN} = "/install/*"
FILES_${PN}-answerfiles = "/*.ans"
FILES_${PN}-part2 = "/*"

RDEPENDS_${PN} = " \
    busybox \
    xenclient-eula \
    xenclient-keyboard-list \
    xenclient-repo-certs \
    xenclient-caps \
"
RDEPENDS_${PN}-part2 += " \
    busybox \
"
