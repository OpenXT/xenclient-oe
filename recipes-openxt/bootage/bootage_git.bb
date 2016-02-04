DESCRIPTION = "bootage"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/bootage.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
	   file://bootage.conf-${PACKAGE_ARCH}"

S = "${WORKDIR}/git"

ASNEEDED = ""

inherit autotools-brokensep xenclient

do_install_append() {
    install -d ${D}/etc
    install -m 644 ${WORKDIR}/bootage.conf-${PACKAGE_ARCH} ${D}/etc/bootage.conf
}
