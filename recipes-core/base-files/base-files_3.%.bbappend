FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
    file://fstab.early \
    file://openxt-aliases.sh \
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
# OpenXT: UIVM has only root as user, pre-create local conf directories.
dirs755_append_xenclient-uivm = " \
    /root/.gconf \
    /root/.gnome2 \
    /root/.cache \
    /root/.ssh \
"

volatiles = ""
conffiles = " \
    ${sysconfdir}/host.conf \
    ${sysconfdir}/issue \
    ${sysconfdir}/issue.net \
    ${sysconfdir}/profile \
    ${sysconfdir}/default \
"

do_install_append() {
    install -m 0644 ${WORKDIR}/fstab.early ${D}${sysconfdir}/fstab.early
    install -m 0755 -d ${D}${sysconfdir}/profile.d
    install -m 0644 ${WORKDIR}/openxt-aliases.sh ${D}${sysconfdir}/profile.d/openxt-aliases.sh
}
