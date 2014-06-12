DESCRIPTION = "XenClient config partition access"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "file://config-access.initscript \
"

INITSCRIPT_NAME = "xenclient-config-access"
INITSCRIPT_PARAMS = "start 32 S ."

S = "${WORKDIR}"

inherit update-rc.d xenclient

do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/config-access.initscript \
		${D}${sysconfdir}/init.d/xenclient-config-access
}
