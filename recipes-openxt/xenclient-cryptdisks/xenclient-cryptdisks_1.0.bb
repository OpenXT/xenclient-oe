DESCRIPTION = "XenClient non-root encrypted partitions access"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

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
