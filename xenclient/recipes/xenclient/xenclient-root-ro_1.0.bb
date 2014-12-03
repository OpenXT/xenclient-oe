LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit xenclient

SRC_URI = "file://init.root-ro \
"

do_install() {
	install -d ${D}/sbin
	install -m 0755 ${WORKDIR}/init.root-ro ${D}/sbin/init.root-ro
}

FILES_${PN} = "/sbin/init.root-ro \
"
