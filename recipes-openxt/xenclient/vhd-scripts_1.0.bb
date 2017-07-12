DESCRIPTION = "XenClient vhd tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://vhd-copy \
           file://vhd-dm-create \
           file://vhd-dm-remove \
"

S = "${WORKDIR}"

inherit xenclient

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/vhd-copy ${D}${bindir}
	install -m 0755 ${WORKDIR}/vhd-dm-create ${D}${bindir}
	install -m 0755 ${WORKDIR}/vhd-dm-remove ${D}${bindir}
}
