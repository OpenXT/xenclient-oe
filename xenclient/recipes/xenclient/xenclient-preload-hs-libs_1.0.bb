DESCRIPTION = "loads haskell libs into file system cache which reduces boot time"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

inherit xenclient
RDEPENDS += " util-linux-ionice "
PACKAGES = "${PN}"

SRC_URI = "file://preload \
"

S = "${WORKDIR}"

do_install() {
	install -d ${D}/etc/init.d/
	install -m 0755 ${WORKDIR}/preload ${D}/etc/init.d/preload
}
