DESCRIPTION = "XenClient vhd tools"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://vhd-copy \
           file://vhd-dm-create \
           file://vhd-dm-remove \
"

S = "${UNPACKDIR}"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${UNPACKDIR}/vhd-copy ${D}${bindir}
	install -m 0755 ${UNPACKDIR}/vhd-dm-create ${D}${bindir}
	install -m 0755 ${UNPACKDIR}/vhd-dm-remove ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
