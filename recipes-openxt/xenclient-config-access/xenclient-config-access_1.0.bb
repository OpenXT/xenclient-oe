DESCRIPTION = "XenClient config partition access"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://config-access.initscript \
"

INITSCRIPT_NAME = "xenclient-config-access"
INITSCRIPT_PARAMS = "start 32 S ."

S = "${UNPACKDIR}"

inherit update-rc.d

do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${UNPACKDIR}/config-access.initscript \
		${D}${sysconfdir}/init.d/xenclient-config-access
}
