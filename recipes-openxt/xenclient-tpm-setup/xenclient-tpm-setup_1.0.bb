DESCRIPTION = "XenClient tpm setup tool"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS:${PN} += "xenclient-tpm-scripts \
	openxt-keymanagement \
	openxt-measuredlaunch \
"

SRC_URI = "file://tpm-setup \
	file://tpm-setup-squashfs \
	file://tpm-setup-quotekey \
"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${UNPACKDIR}/tpm-setup ${D}${bindir}
	install -m 0755 ${UNPACKDIR}/tpm-setup-squashfs ${D}${bindir}
	install -m 0755 ${UNPACKDIR}/tpm-setup-quotekey ${D}${bindir}
}
