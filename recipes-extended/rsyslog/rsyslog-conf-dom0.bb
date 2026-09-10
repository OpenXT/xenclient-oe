DESCRIPTION = "db tools"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit allarch

SRC_URI = "file://rsyslog.conf"

RPROVIDES:${PN} = "rsyslog-conf"
RCONFLICTS:${PN} = "rsyslog-conf"

CONFFILES:${PN} = "${sysconfdir}/rsyslog.conf"

do_install() {
    install -d ${D}${sysconfdir}
    install -m 644 ${UNPACKDIR}/rsyslog.conf ${D}${sysconfdir}
}
