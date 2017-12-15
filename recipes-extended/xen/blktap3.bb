DESCRIPTION = "blktap3"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=a9e8593dad23434929bc6218a17b5634"
DEPENDS = "xen" 

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/blktap3.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
	file://tapback.initscript \
"

S = "${WORKDIR}/git"

inherit autotools-brokensep xenclient update-rc.d

INITSCRIPT_NAME = "tapback-daemon"
INITSCRIPT_PARAMS = "defaults 61"

do_configure_prepend() {
	touch ${S}/EXTRAVERSION
}

do_install_append() {
	rm -rf ${D}/usr/lib/systemd
	install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/tapback.initscript \
		${D}/etc/init.d/tapback-daemon
}

RDEPENDS_${PN} += "glibc-gconv-utf-16"
