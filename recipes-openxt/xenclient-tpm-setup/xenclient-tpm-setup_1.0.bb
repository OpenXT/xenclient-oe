DESCRIPTION = "XenClient tpm setup tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit xenclient

RDEPENDS_${PN} += "xenclient-tpm-scripts \
	openxt-keymanagement \
	openxt-measuredlaunch \
"

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
