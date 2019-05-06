DESCRIPTION = "scripts to aid in the configuration and maintenance of the TPM"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit xenclient

SRC_URI = " \
    file://tpm-functions \
    file://*-detect.sh \
    file://*-fix.sh \
"

RDEPENDS_${PN} = " \
    tpm-tools \
    tpm2-tools \
"

FILES_${PN} = "${libdir}/tpm-scripts"

do_install() {
	install -d ${D}${libdir}/tpm-scripts
	install -m 0755 ${WORKDIR}/tpm-functions ${D}${libdir}/tpm-scripts
	install -d ${D}${libdir}/tpm-scripts/quirks.d
	for detect in $(ls -1 ${WORKDIR}/*-detect.sh); do
		install -m 0755 ${detect} ${D}${libdir}/tpm-scripts/quirks.d
	done
	for fix in $(ls -1 ${WORKDIR}/*-fix.sh); do
		install -m 0755 ${fix} ${D}${libdir}/tpm-scripts/quirks.d
	done
}
