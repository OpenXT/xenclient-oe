DESCRIPTION = "XenClient pcr diff tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit xenclient

SRC_URI = " \
           file://pcr-state\
           file://pcr-data\
"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/pcr-state ${D}${bindir}
	install -m 0755 ${WORKDIR}/pcr-data ${D}${bindir}
}
