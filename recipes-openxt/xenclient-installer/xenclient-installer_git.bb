DESCRIPTION = "XenClient Installer"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = " \
    git://github.com/OpenXT/installer.git;protocol=https;branch=master \
    file://network.ans \
    file://network_download_win.ans \
    file://network_manual.ans \
    file://network_manual_download_win.ans \
    file://network_upgrade.ans \
"


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
        install -m 0644 "${UNPACKDIR}/${f}" "${D}/${f}"
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

FILES:${PN} = "/install/*"
FILES:${PN}-answerfiles = "/*.ans"
FILES:${PN}-part2 = "/*"

RDEPENDS:${PN} = " \
    busybox \
    xenclient-eula \
    xenclient-keyboard-list \
    xenclient-repo-certs \
    xenclient-caps \
"
RDEPENDS:${PN}-part2 += " \
    busybox \
"
