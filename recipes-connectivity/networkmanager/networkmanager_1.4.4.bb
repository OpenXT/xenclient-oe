SUMMARY = "NetworkManager"
SECTION = "net/misc"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=cbbffd568227ada506640fe950a4823b \
                    file://libnm-util/COPYING;md5=1c4fa765d6eb3cd2fbd84344a1b816cd \
                    file://docs/api/html/license.html;md5=8eb8e72bab097b9d11763002cb003697 \
"

DEPENDS = " \
    intltool-native \
    libnl \
    dbus \
    dbus-glib \
    dbus-glib-native \
    udev \
    ppp \
    nss \
    libgudev \
    util-linux \
    libndp \
    libnewt \
    jansson \
"

inherit gnomebase gettext systemd bash-completion vala gobject-introspection

SRC_URI = "${GNOME_MIRROR}/NetworkManager/${@gnome_verdir("${PV}")}/NetworkManager-${PV}.tar.xz \
           file://0001-don-t-try-to-run-sbin-dhclient-to-get-the-version-nu.patch \
           file://0002-Fix-nm-version-macro-includes.patch \
           file://0001-adjust-net-headers-for-musl-compatibility.patch \
           file://0002-socket-util.h-Include-linux-sockios.h-on-musl.patch \
           file://0003-Define-ETH_ALEN.patch \
           file://0004-Define-missing-features-to-cater-for-musl.patch \
           file://0005-sd-lldp.h-Remove-net-ethernet.h-seems-to-be-over-spe.patch \
           file://0001-check-for-strndupa-before-using-it.patch \
           file://fix-errors-due-to-glib-upgrade.patch \
           file://fix-compatibility-with-network-slave.patch \
           file://auto-dhcp-for-eth0.patch \
           file://db-nm-settings.patch \
           file://use-dom0-db-for-seen-bssids.patch \
           file://only-request-secrets-on-initial-connect.patch \
           file://always-assume-uid-for-external-processes.patch \
           file://fix-device-type-property-exposure.patch \
           file://update-resolv-conf.patch \
           file://update-routing-tables.patch \
           file://fix-eth0-slave-issues.patch \
           file://NetworkManager.conf \
           file://nm_sync.sh \
           file://db_to_nm.awk \
           file://nm_to_db.awk \
           file://org.openxt.nmapplet.conf \
           file://dbus-system.conf \
"

SRC_URI[md5sum] = "63f1e0d6d7e9099499d062c84c927a75"
SRC_URI[sha256sum] = "829378f318cc008d138a23ca6a9191928ce75344e7e47a2f2c35f4ac82133309"

S = "${WORKDIR}/NetworkManager-${PV}"

EXTRA_OECONF = " \
    --disable-ifcfg-rh \
    --disable-ifnet \
    --disable-ifcfg-suse \
    --disable-more-warnings \
    --with-iptables=${sbindir}/iptables \
    --with-tests \
    --with-nmtui=yes \
    --with-wwan=no \
    --enable-more-logging \
    --disable-polkit \
    --without-systemdsystemunitdir \
"

do_compile_prepend() {
        export GIR_EXTRA_LIBS_PATH="${B}/libnm-util/.libs:${B}/libnm-glib/.libs"
}

PACKAGECONFIG ??= "nss ifupdown netconfig dhclient dnsmasq \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', 'consolekit', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez5', '', d)} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wifi polkit', d)} \
"
PACKAGECONFIG[systemd] = " \
    --with-systemdsystemunitdir=${systemd_unitdir}/system --with-session-tracking=systemd --enable-polkit, \
    --without-systemdsystemunitdir, \
    polkit \
"
PACKAGECONFIG[polkit] = "--enable-polkit --enable-polkit-agent,--disable-polkit --disable-polkit-agent,polkit"
PACKAGECONFIG[bluez5] = "--enable-bluez5-dun,--disable-bluez5-dun,bluez5"
# consolekit is not picked by shlibs, so add it to RDEPENDS too
PACKAGECONFIG[consolekit] = "--with-session-tracking=consolekit,,consolekit,consolekit"
PACKAGECONFIG[concheck] = "--with-libsoup=yes,--with-libsoup=no,libsoup-2.4"
PACKAGECONFIG[modemmanager] = "--with-modem-manager-1=yes,--with-modem-manager-1=no,modemmanager"
PACKAGECONFIG[ppp] = "--enable-ppp,--disable-ppp,ppp,ppp"
# Use full featured dhcp client instead of internal one
PACKAGECONFIG[dhclient] = "--with-dhclient=${base_sbindir}/dhclient,,,dhcp-client"
PACKAGECONFIG[dnsmasq] = "--with-dnsmasq=${bindir}/dnsmasq"
PACKAGECONFIG[nss] = "--with-crypto=nss,,nss"
PACKAGECONFIG[gnutls] = "--with-crypto=gnutls,,gnutls"
PACKAGECONFIG[wifi] = "--enable-wifi=yes,--enable-wifi=no,,wpa-supplicant"
PACKAGECONFIG[ifupdown] = "--enable-ifupdown,--disable-ifupdown"
PACKAGECONFIG[netconfig] = "--with-netconfig=yes,--with-netconfig=no"
PACKAGECONFIG[qt4-x11-free] = "--enable-qt,--disable-qt,qt4-x11-free"

do_install_prepend () {
        sed -i -e s:deny:allow:g ${S}/src/org.freedesktop.NetworkManager.conf
        sed -i -e s:deny:allow:g ${S}/callouts/nm-dispatcher.conf
} 

do_install_append () {
    install -m 0755 -d ${D}${sysconfdir}/dbus-1
    install -m 0755 -d ${D}${sysconfdir}/dbus-1/event.d
    #Additional test binaries
    install -d ${D}${bindir}

    install -m 0755 -d ${D}${sysconfdir}/NetworkManager
    rm -f ${D}${sysconfdir}/init.d/NetworkManager
    install -d ${D}${nmidldatadir}
    install -m 0644 ${S}/introspection/*.xml ${D}${nmidldatadir}/
    install -m 0644 ${S}/openxt/*.xml ${D}${nmidldatadir}/
    install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}/etc/NetworkManager

    # OpenXT additional scripts for NDVM.
    install -m 0755 ${WORKDIR}/nm_sync.sh ${D}${bindir}/nm_sync.sh
    install -m 0755 -d ${D}${datadir}/xenclient/nm_scripts
    install -m 0755 ${WORKDIR}/db_to_nm.awk ${D}${datadir}/xenclient/nm_scripts/db_to_nm.awk
    install -m 0755 ${WORKDIR}/nm_to_db.awk ${D}${datadir}/xenclient/nm_scripts/nm_to_db.awk
    install -m 0644 ${D}${sysconfdir}/NetworkManager/NetworkManager.conf ${D}${datadir}/xenclient/nm_scripts/

    # Install dbus conf file for allowing nm-applet to own a bus name
    install -m 0755 ${WORKDIR}/org.openxt.nmapplet.conf ${D}${sysconfdir}/dbus-1/system.d/org.openxt.nmapplet.conf

    # Install system-local.conf file to allow nm-applet to connect to system dbus.
    install -m 0644 ${WORKDIR}/dbus-system.conf ${D}${sysconfdir}/dbus-1/system-local.conf

    # oe default
    rm -rf ${D}/run ${D}${localstatedir}/run
}

PACKAGES =+ "libnmutil libnmglib libnmglib-vpn \
  ${PN}-nmtui ${PN}-nmtui-doc \
  ${PN}-adsl \
"

FILES_libnmutil += "${libdir}/libnm-util.so.*"
FILES_libnmglib += "${libdir}/libnm-glib.so.*"
FILES_libnmglib-vpn += "${libdir}/libnm-glib-vpn.so.*"

FILES_${PN}-adsl = "${libdir}/NetworkManager/libnm-device-plugin-adsl.so"

FILES_${PN} += " \
    ${libexecdir} \
    ${libdir}/pppd/*/nm-pppd-plugin.so \
    ${libdir}/NetworkManager/*.so \
    ${datadir}/polkit-1 \
    ${datadir}/dbus-1 \
    ${base_libdir}/udev/* \
    ${systemd_unitdir}/system \
    ${nmidldatadir} \
"
FILES_${PN} += " \
    ${datadir}/xenclient/nm_scripts/db_to_nm.awk \
    ${datadir}/xenclient/nm_scripts/nm_to_db.awk \
    ${datadir}/xenclient/nm_scripts/NetworkManager.conf \
    ${bindir}/nm_sync.sh \
"


RRECOMMENDS_${PN} += "iptables \
    ${@bb.utils.filter('PACKAGECONFIG', 'dnsmasq', d)} \
"

RCONFLICTS_${PN} = "connman"

RDEPENDS_${PN} = " \
    wpa-supplicant \
    dhcp-client \
    ${@bb.utils.contains('COMBINED_FEATURES', '3gmodem', 'ppp', '', d)} \
    libgudev \
    dnsmasq \
    iproute2 \
    networkmanager-certs \
"

FILES_${PN}-dev += " \
    ${datadir}/NetworkManager/gdb-cmd \
    ${libdir}/pppd/*/*.la \
    ${libdir}/NetworkManager/*.la \
"
FILES_${PN}-dbg += " \
    ${libdir}/NetworkManager/.debug/ \
    ${libdir}/pppd/*/.debug/ \
"
FILES_${PN}-nmtui = " \
    ${bindir}/nmtui \
    ${bindir}/nmtui-edit \
    ${bindir}/nmtui-connect \
    ${bindir}/nmtui-hostname \
"

FILES_${PN}-nmtui-doc = " \
    ${mandir}/man1/nmtui* \
"

#SYSTEMD_SERVICE_${PN} = "NetworkManager.service NetworkManager-dispatcher.service"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE = ""
