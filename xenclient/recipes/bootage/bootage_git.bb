DESCRIPTION = "bootage"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "${OPENXT_GIT_MIRROR}/bootage.git;protocol=git;tag=${OPENXT_TAG} \
	   file://bootage.conf-${PACKAGE_ARCH}"

S = "${WORKDIR}/git"

inherit autotools
inherit xenclient

do_install_append() {
    install -d ${D}/etc
    install -m 644 ${WORKDIR}/bootage.conf-${PACKAGE_ARCH} ${D}/etc/bootage.conf
}
