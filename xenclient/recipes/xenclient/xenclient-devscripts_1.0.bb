LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
RDEPENDS = "compleat"
PR = "r2"

SRC_URI += " file://helper.sh	\
           "
do_install () {
	install -d ${D}/usr/bin
	install -m 0755 ${WORKDIR}/helper.sh	\
		${D}/usr/bin/helper.sh
}
