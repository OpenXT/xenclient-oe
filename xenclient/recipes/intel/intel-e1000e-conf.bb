DESCRIPTION = "Configuration files for Intel e1000e Ether driver"

# This part is configuration for OpenXT so we cover it under the
# default license.
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
SRC_URI = "file://e1000e-power.conf"

do_install() {
	install -d ${D}/etc/modprobe.d/
	install -m 0644 ${WORKDIR}/e1000e-power.conf ${D}/etc/modprobe.d/
}

FILES_${PN} = "/etc/modprobe.d/"
