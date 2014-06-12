DESCRIPTION = "scripts to aid in the configuration and maintenance of the TPM"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

inherit xenclient

SRC_URI = " \
    file://tpm-functions \
    file://*-detect.sh \
    file://*-fix.sh \
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
