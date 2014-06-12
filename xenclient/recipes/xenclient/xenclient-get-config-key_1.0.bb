DESCRIPTION = "XenClient config partition key tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

inherit xenclient

SRC_URI = "file://get-config-key.c \
"

S = "${WORKDIR}"

do_compile() {
	oe_runmake get-config-key
	${STRIP} get-config-key
}

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/get-config-key ${D}${sbindir}
}
