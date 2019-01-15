DESCRIPTION = "XenClient config partition access"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

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
