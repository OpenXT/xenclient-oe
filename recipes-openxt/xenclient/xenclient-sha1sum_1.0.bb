DESCRIPTION = "XenClient sha1 tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS = "openssl"

PR = "r0"

SRC_URI = "file://xc-sha1sum.c \
"

S = "${WORKDIR}"

ASNEEDED = ""

do_compile() {
	oe_runmake xc-sha1sum LDFLAGS="-lssl -lcrypto"
	${STRIP} xc-sha1sum
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/xc-sha1sum ${D}${bindir}
}

# Avoid GNU_HASH check
INSANE_SKIP_${PN} = "1"
