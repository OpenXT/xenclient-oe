DESCRIPTION = "XenClient config partition key tool"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    file://get-config-key.c \
"

S = "${UNPACKDIR}"

do_compile() {
	oe_runmake get-config-key
}

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${UNPACKDIR}/get-config-key ${D}${sbindir}
}
