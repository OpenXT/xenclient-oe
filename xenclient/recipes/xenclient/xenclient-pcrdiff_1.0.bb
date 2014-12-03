DESCRIPTION = "XenClient pcr diff tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

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
