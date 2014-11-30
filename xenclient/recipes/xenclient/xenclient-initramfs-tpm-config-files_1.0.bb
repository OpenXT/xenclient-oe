PR="r0"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://initramfs-tcsd.conf \
           file://initramfs-passwd \
           file://initramfs-group \
           file://initramfs-nsswitch.conf \
"

do_install() {
	install -d ${D}${sysconfdir}
	install -m 0600 ${WORKDIR}/initramfs-tcsd.conf ${D}${sysconfdir}/tcsd.conf
	install -m 0644 ${WORKDIR}/initramfs-passwd ${D}${sysconfdir}/passwd
	install -m 0644 ${WORKDIR}/initramfs-group ${D}${sysconfdir}/group
	install -m 0644 ${WORKDIR}/initramfs-nsswitch.conf ${D}${sysconfdir}/nsswitch.conf
}

FILES_${PN} = "${sysconfdir}/tcsd.conf \
               ${sysconfdir}/passwd \
               ${sysconfdir}/group \
               ${sysconfdir}/nsswitch.conf \
"
