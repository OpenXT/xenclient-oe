FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://logrotate.conf \
    file://logrotate-wrapper \
    "

# Do not ship logrotate.status file, wrapper will create it in /tmp as domains are RO...
CONFFILES:${PN} = " \
    ${sysconfdir}/logrotate.conf \
    "
# ... also install our configuration and wrapper (for rsyslog).
do_install:append() {
    rm -f ${D}${localstatedir}/lib/logrotate.status
    install -p -m 644 ${UNPACKDIR}/logrotate.conf ${D}${sysconfdir}/logrotate.conf
    install -p -m 755 ${UNPACKDIR}/logrotate-wrapper ${D}${sbindir}/logrotate-wrapper
}

INSANE_SKIP:${PN} = "src-uri-bad"
