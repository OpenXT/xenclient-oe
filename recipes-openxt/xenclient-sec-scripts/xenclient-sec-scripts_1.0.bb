DESCRIPTION = "XenClient sec-* tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit xenclient

SRC_URI = "file://sec-change-pass \
	file://sec-change-recovery \
	file://sec-check-pass \
	file://sec-check-user \
	file://sec-mount \
	file://sec-new-user \
	file://sec-umount \
        file://sec-rm-user \
        file://rm-platform-user \
        file://sec-change-root-credentials \
        file://sec-new-user-without-password \
"

S = "${WORKDIR}"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-change-pass ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-change-recovery ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-check-pass ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-check-user ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-mount ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-new-user ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-umount ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-rm-user ${D}${bindir}
	install -m 0755 ${WORKDIR}/rm-platform-user ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-change-root-credentials ${D}${bindir}
	install -m 0755 ${WORKDIR}/sec-new-user-without-password ${D}${bindir}
}
