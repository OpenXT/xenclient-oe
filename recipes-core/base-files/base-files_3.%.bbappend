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
# Add /var/log back as OpenXT uses encrypted partition for it (... and
# installer is ramfs)
# Add /media/ram for the key-functions scripts (mount point).
dirs755_append = " \
    ${localstatedir}/log \
    /media/ram \
"
# Remove the volatile directories. OpenXT does not leverage them.
dirs755_remove = " \
    ${localstatedir}/volatile/log \
    ${localstatedir}/volatile/tmp \
"

# OpenXT: Dom0 specific additional directories.
dirs755_append_xenclient-dom0 = " \
    /storage \
    ${localstatedir}/cores \
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
