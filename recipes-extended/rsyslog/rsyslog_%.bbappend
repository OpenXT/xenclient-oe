# Fetch our configuration files.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Hack until upstream fix is backported on Pyro upstream:
# 32a93e0d5 rsyslog: install logrotate configuration file into correct location
do_install_append() {
    rm -f ${D}${sysconfdir}/logrotate.d/logrotate.rsyslog
    install -d "${D}${sysconfdir}/logrotate.d"
    install -m 644 ${WORKDIR}/rsyslog.logrotate ${D}${sysconfdir}/logrotate.d/rsyslog
}
