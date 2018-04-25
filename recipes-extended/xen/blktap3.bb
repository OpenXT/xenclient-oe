DESCRIPTION = "blktap3"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=a9e8593dad23434929bc6218a17b5634"
DEPENDS = "xen libicbinn"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/blktap3.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
    file://tapback.initscript \
    file://blktap3-vhd-icbinn-support.patch \
"

S = "${WORKDIR}/git"

inherit autotools-brokensep xenclient update-rc.d

INITSCRIPT_NAME = "tapback-daemon"
INITSCRIPT_PARAMS = "defaults 61"

do_configure_prepend() {
	touch ${S}/EXTRAVERSION
}

do_install_append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
		rm -rf ${D}/usr/lib/systemd
	fi
	install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/tapback.initscript \
		${D}/etc/init.d/tapback-daemon
}

# QA dev-elf: libvhdio-3.5.0.so does not honor the SOLIBSDEV format.
FILES_SOLIBSDEV = ""
FILES_${PN}-dev += " \
    ${libdir}/libblktapctl.so \
    ${libdir}/libvhd.so \
    ${libdir}/libvhdio.so \
"
FILES_${PN} += " \
    ${libdir}/libvhdio-*.so \
"
RDEPENDS_${PN} += "glibc-gconv-utf-16"
RCONFLICTS_${PN} = "xen-blktap xen-libblktap xen-libblktapctl xen-libvhd"
