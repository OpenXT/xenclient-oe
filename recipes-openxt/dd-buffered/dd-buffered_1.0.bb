DESCRIPTION = "XenClient buffered dd tool"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PR = "r1"

SRC_URI = "file://dd-buffered.c \
"

S = "${UNPACKDIR}"

do_compile() {
	oe_runmake dd-buffered
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${UNPACKDIR}/dd-buffered ${D}${bindir}
}
