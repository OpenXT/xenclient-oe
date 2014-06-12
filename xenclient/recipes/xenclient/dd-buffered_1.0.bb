DESCRIPTION = "XenClient buffered dd tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

PR = "r1"

SRC_URI = "file://dd-buffered.c \
"

S = "${WORKDIR}"

do_compile() {
	oe_runmake dd-buffered
	${STRIP} dd-buffered
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/dd-buffered ${D}${bindir}
}
