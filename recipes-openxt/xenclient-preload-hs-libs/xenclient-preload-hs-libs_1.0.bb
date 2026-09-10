DESCRIPTION = "loads haskell libs into file system cache which reduces boot time"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS:${PN} += "util-linux"
PACKAGES = "${PN}"

SRC_URI = "file://preload \
"

S = "${UNPACKDIR}"

do_install() {
	install -d ${D}/etc/init.d/
	install -m 0755 ${UNPACKDIR}/preload ${D}/etc/init.d/preload
}
