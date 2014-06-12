DESCRIPTION = "Simple poweroff implementation for stubdoms"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "file://poweroff.c \
"

S = "${WORKDIR}"

inherit xenclient

do_compile() {
	oe_runmake poweroff
}

do_install() {
	install -d ${D}/sbin
	install -m 0755 ${WORKDIR}/poweroff ${D}/sbin
}
