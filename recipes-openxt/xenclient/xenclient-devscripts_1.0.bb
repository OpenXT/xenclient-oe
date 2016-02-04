LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
RDEPENDS_${PN} = "compleat"
PR = "r2"

SRC_URI += " file://helper.sh	\
           "
do_install () {
	install -d ${D}/usr/bin
	install -m 0755 ${WORKDIR}/helper.sh	\
		${D}/usr/bin/helper.sh
}
