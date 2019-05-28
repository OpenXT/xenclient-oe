DESCRIPTION = "scripts to aid in the configuration and maintenance of key management"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit xenclient

SRC_URI = " \
    file://key-functions \
"

FILES_${PN} = "${libdir}/openxt/key-functions"

do_install() {
	install -d ${D}${libdir}/openxt
	install -m 0755 ${WORKDIR}/key-functions ${D}${libdir}/openxt
}

RDEPENDS_${PN} = " \
    xenclient-tpm-scripts \
    tpm-tools-sa \
"
