DESCRIPTION = "XenClient sha1 tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "openssl"

PR = "r0"

SRC_URI = "file://xc-sha1sum.c \
"

S = "${WORKDIR}"

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
