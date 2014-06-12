DESCRIPTION = "XenClient non-root encrypted partitions access"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

RDEPENDS_${PN} = "cryptsetup"

SRC_URI = "file://cryptdisks.initscript \
           file://crypttab              \
"

INITSCRIPT_NAME = "cryptdisks"
INITSCRIPT_PARAMS = "start 34 S ."

S = "${WORKDIR}"

inherit update-rc.d xenclient

do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/cryptdisks.initscript \
		${D}${sysconfdir}/init.d/cryptdisks
	install -m 0755 ${WORKDIR}/crypttab \
		${D}${sysconfdir}/crypttab
}
