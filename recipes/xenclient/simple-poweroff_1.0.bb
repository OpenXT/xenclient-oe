DESCRIPTION = "Simple poweroff implementation for stubdoms"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://poweroff.c \
"

S = "${WORKDIR}"

inherit xenclient

do_compile() {
	oe_runmake poweroff
}

do_install() {
	install -d ${D}/sbin
	install -m 0755 ${WORKDIR}/poweroff ${D}/sbin
}
