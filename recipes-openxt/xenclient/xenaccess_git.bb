
inherit xenclient
inherit autotools-brokensep pkgconfig lib_package

DEPENDS = "xen-tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d6058eb45f5694b85b31237bef240081"

SRCREV_source = "${AUTOREV}"

PV = "0+git${SRCPV}"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/xenaccess.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH};name=source \
    file://detect-guest-address-width.patch;patch=1 \
    file://hack-to-auto-detect-windows-guest-version.patch;patch=1 \
"

PARALLEL_MAKE = ""

S = "${WORKDIR}/git/libxa"

do_install_append() {
	mv ${D}/usr/lib/libxenaccess-0.5.so ${D}/usr/lib/libxenaccess.so.0.5
	ln -sf libxenaccess.so.0.5 ${D}/usr/lib/libxenaccess.so
}
