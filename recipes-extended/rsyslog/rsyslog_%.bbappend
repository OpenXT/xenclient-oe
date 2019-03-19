# Fetch our configuration files.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:${THISDIR}/patches:"

SRC_URI += " \
    file://CVE-2017-12588.patch \
    file://0001-core-bugfix-segfault-after-configuration-errors.patch \
"

# Hack until upstream fix is backported on Pyro upstream:
# 32a93e0d5 rsyslog: install logrotate configuration file into correct location
do_install_append() {
    rm -f ${D}${sysconfdir}/logrotate.rsyslog
    install -d "${D}${sysconfdir}/logrotate.d"
    install -m 644 ${WORKDIR}/rsyslog.logrotate ${D}${sysconfdir}/logrotate.d/rsyslog
}

PACKAGES =+ "${PN}-conf"
RRECOMMENDS_${PN} += "${PN}-conf"

RSYSLOG_CONF = "${sysconfdir}/rsyslog.conf"
CONFFILES_${PN}-conf += "${RSYSLOG_CONF}"
FILES_${PN}-conf = "${RSYSLOG_CONF}"
