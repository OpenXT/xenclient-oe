DESCRIPTION = "XenClient tpm setup tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

inherit xenclient

RDEPENDS += "xenclient-tpm-scripts"

SRC_URI = "file://tpm-setup \
	file://tpm-setup-squashfs \
	file://tpm-setup-quotekey \
"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/tpm-setup ${D}${bindir}
	install -m 0755 ${WORKDIR}/tpm-setup-squashfs ${D}${bindir}
	install -m 0755 ${WORKDIR}/tpm-setup-quotekey ${D}${bindir}
}
