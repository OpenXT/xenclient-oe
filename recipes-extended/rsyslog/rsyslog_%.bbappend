# Fetch our configuration files.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:${THISDIR}/patches:"

PACKAGES =+ "${PN}-conf"
RRECOMMENDS_${PN} += "${PN}-conf"

RSYSLOG_CONF = "${sysconfdir}/rsyslog.conf"
CONFFILES_${PN}-conf += "${RSYSLOG_CONF}"
FILES_${PN}-conf = "${RSYSLOG_CONF}"
