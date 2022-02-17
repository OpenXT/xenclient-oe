DESCRIPTION = "db tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit allarch

SRC_URI = "file://rsyslog.conf"

RPROVIDES_${PN} = "rsyslog-conf"
RCONFLICTS_${PN} = "rsyslog-conf"

CONFFILES_${PN} = "${sysconfdir}/rsyslog.conf"

do_install() {
    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/rsyslog.conf ${D}${sysconfdir}
}
