DESCRIPTION = "bootage"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0+git${SRCPV}"

SRCREV = "ebb0cdcbdeff450a411dde195edbc0eb6b8b369b"
SRC_URI = "git://github.com/openxt/bootage.git;protocol=https \
	   file://bootage.conf-${PACKAGE_ARCH}"

S = "${WORKDIR}/git"

inherit autotools
inherit xenclient

do_install_append() {
    install -d ${D}/etc
    install -m 644 ${WORKDIR}/bootage.conf-${PACKAGE_ARCH} ${D}/etc/bootage.conf
}
