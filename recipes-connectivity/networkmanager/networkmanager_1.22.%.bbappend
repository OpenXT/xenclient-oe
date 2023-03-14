FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:${THISDIR}/files:"

SRC_URI += " \
    file://fix-compatibility-with-network-slave.patch \
    file://always-assume-uid-for-external-processes.patch \
    file://auto-dhcp.patch \
    file://db-nm-settings.patch \
    file://fix-eth0-slave-issues.patch \
    file://update-resolv-conf.patch \
    file://update-routing-tables.patch \
    file://use-dom0-db-for-seen-bssids.patch \
    file://disable-ipv6-config.patch \
    file://fix-network-reenable.patch \
    file://NetworkManager.conf \
    file://nm_sync.sh \
    file://db_to_nm.awk \
    file://nm_to_db.awk \
    file://org.openxt.nmapplet.conf \
    file://dbus-system.conf \
"

do_install_append () {
    install -m 0755 -d ${D}${nmidldatadir}
    install -m 0644 ${S}/introspection/*.xml ${D}${nmidldatadir}/
    install -m 0644 ${S}/openxt/*.xml ${D}${nmidldatadir}/
    install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}/etc/NetworkManager/

    install -m 0755 ${WORKDIR}/nm_sync.sh ${D}${bindir}/nm_sync.sh
    install -m 0755 -d ${D}${datadir}/xenclient/nm_scripts
    install -m 0755 ${WORKDIR}/db_to_nm.awk ${D}${datadir}/xenclient/nm_scripts/db_to_nm.awk
    install -m 0755 ${WORKDIR}/nm_to_db.awk ${D}${datadir}/xenclient/nm_scripts/nm_to_db.awk
    install -m 0644 ${D}${sysconfdir}/NetworkManager/NetworkManager.conf ${D}${datadir}/xenclient/nm_scripts/

    # Install dbus conf file for allowing nm-applet to own a bus name
    install -m 0755 ${WORKDIR}/org.openxt.nmapplet.conf ${D}${sysconfdir}/dbus-1/system.d/org.openxt.nmapplet.conf

    # Install system-local.conf file to allow nm-applet to connect to system dbus.
    install -m 0644 ${WORKDIR}/dbus-system.conf ${D}${sysconfdir}/dbus-1/system-local.conf
}

FILES_${PN} += " \
    ${nmidldatadir} \
    ${datadir}/xenclient/nm_scripts/db_to_nm.awk \
    ${datadir}/xenclient/nm_scripts/nm_to_db.awk \
    ${datadir}/xenclient/nm_scripts/NetworkManager.conf \
    ${bindir}/nm_sync.sh \
"

# Disable initscript
INITSCRIPT_PARAMS = "disable 2 3 4 5"
