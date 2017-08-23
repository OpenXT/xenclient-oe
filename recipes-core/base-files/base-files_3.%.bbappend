FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
    file://inputrc \
"
dirs1777 = " \
    /tmp \
    ${localstatedir}/lock \
    ${localstatedir}/tmp \
"
dirs2775 = " \
    /home \
    ${prefix}/src \
    ${localstatedir}/local \
"
volatiles = ""
conffiles = " \
    ${sysconfdir}/host.conf \
    ${sysconfdir}/inputrc \
    ${sysconfdir}/issue \
    ${sysconfdir}/issue.net \
    ${sysconfdir}/profile \
    ${sysconfdir}/default \
"
