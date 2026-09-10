DESCRIPTION = "XenClient non-root encrypted partitions access"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS:${PN} = "cryptsetup"

SRC_URI = "file://cryptdisks.initscript \
           file://crypttab              \
"

INITSCRIPT_NAME = "cryptdisks"
INITSCRIPT_PARAMS = "start 34 S ."

S = "${UNPACKDIR}"

inherit update-rc.d

do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${UNPACKDIR}/cryptdisks.initscript \
		${D}${sysconfdir}/init.d/cryptdisks
	install -m 0755 ${UNPACKDIR}/crypttab \
		${D}${sysconfdir}/crypttab
}
