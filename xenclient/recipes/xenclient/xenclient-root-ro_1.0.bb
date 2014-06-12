LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

inherit xenclient

SRC_URI = "file://init.root-ro \
"

do_install() {
	install -d ${D}/sbin
	install -m 0755 ${WORKDIR}/init.root-ro ${D}/sbin/init.root-ro
}

FILES_${PN} = "/sbin/init.root-ro \
"
