DESCRIPTION = "scripts to aid in the configuration and maintenance of key management"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    file://key-functions \
"

FILES:${PN} = "${libdir}/openxt/key-functions"

do_install() {
	install -d ${D}${libdir}/openxt
	install -m 0755 ${UNPACKDIR}/key-functions ${D}${libdir}/openxt
}

RDEPENDS:${PN} = " \
    xenclient-tpm-scripts \
    tpm-tools-sa \
"
