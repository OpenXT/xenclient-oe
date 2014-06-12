DESCRIPTION = "XenClient secure vm tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

PR = "r0"

SRC_URI = "file://secure-vm \
"

S = "${WORKDIR}"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/secure-vm ${D}${bindir}
}
