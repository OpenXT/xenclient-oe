# Fetch our configuration files.
FILESEXTRAPATHS:prepend := "${THISDIR}/files:${THISDIR}/patches:"

PACKAGES =+ "${PN}-conf"
RRECOMMENDS:${PN} += "${PN}-conf"

RSYSLOG_CONF = "${sysconfdir}/rsyslog.conf"
CONFFILES:${PN}-conf += "${RSYSLOG_CONF}"
FILES:${PN}-conf = "${RSYSLOG_CONF}"
