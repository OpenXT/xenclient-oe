DESCRIPTION = "XenClient vhd-copy tool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "file://vhd-copy \
"

S = "${WORKDIR}"

inherit xenclient

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/vhd-copy ${D}${bindir}
}
