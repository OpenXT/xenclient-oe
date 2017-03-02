DESCRIPTION = "loads haskell libs into file system cache which reduces boot time"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit xenclient
RDEPENDS_${PN} += "util-linux"
PACKAGES = "${PN}"

SRC_URI = "file://preload \
"

S = "${WORKDIR}"

do_install() {
	install -d ${D}/etc/init.d/
	install -m 0755 ${WORKDIR}/preload ${D}/etc/init.d/preload
}
