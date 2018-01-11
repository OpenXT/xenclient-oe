SUMMARY = "NetworkManager"
SECTION = "net/misc"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=cbbffd568227ada506640fe950a4823b"

DEPENDS = " \
    libnl \
    dbus \
    dbus-glib \
    dbus-glib-native \
    udev \
    wireless-tools \
    nss \
    util-linux \
    ppp \
    intltool-native \
    libgudev \
"

inherit gnome gettext systemd gobject-introspection

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"
SRC_URI = " \
    ${GNOME_MIRROR}/NetworkManager/${@gnome_verdir("${PV}")}/NetworkManager-${PV}.tar.xz \
    file://0001-don-t-try-to-run-sbin-dhclient-to-get-the-version-nu.patch \
    file://0001-configure.ac-Check-only-for-libsystemd-not-libsystem.patch \
    file://db-nm-settings.patch \
    file://use-dom0-db-for-seen-bssids.patch \
    file://rpcgen-does-not-support-dbus-tuples.patch \
    file://NetworkManager.conf \
    file://WiredEthernetConnection \
"

SRC_URI += " \
    file://nm_sync.sh \
    file://db_to_nm.awk \
    file://nm_to_db.awk \
"
SRC_URI[md5sum] = "aad2558887e25417c52eb2deaade2f85"
SRC_URI[sha256sum] = "064d27223d3824859df12e1fb25b787fec1c68bbc864dc52a0289b9211c4c972"


S = "${WORKDIR}/NetworkManager-${PV}"

EXTRA_OECONF = " \
    --enable-ifupdown \
    --disable-ifcfg-rh \
    --disable-ifnet \
    --disable-ifcfg-suse \
    --with-netconfig \
    --with-crypto=nss \
    --disable-more-warnings \
    --with-dhclient=${base_sbindir}/dhclient \
    --with-iptables=${sbindir}/iptables \
    --with-tests \
    --with-dnsmasq=${bindir}/dnsmasq \
    --enable-wimax=no \
"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES','systemd','systemd','consolekit',d)}"
PACKAGECONFIG[systemd] = " \
    --with-systemdsystemunitdir=${systemd_unitdir}/system --with-session-tracking=systemd --enable-polkit, \
    --without-systemdsystemunitdir, \
    polkit \
"
# consolekit is not picked by shlibs, so add it to RDEPENDS too
PACKAGECONFIG[consolekit] = "--with-session-tracking=consolekit,,consolekit,consolekit"
PACKAGECONFIG[concheck] = "--enable-concheck,--disable-concheck,libsoup-2.4"

export GIR_EXTRA_LIBS_PATH="${B}/libnm-util/.libs:${B}/libnm-glib/.libs"

# Work around dbus permission problems since we lack a proper at_console
do_install_prepend() {
    sed -i 's:deny send_destination:allow send_destination:g' ${S}/src/org.freedesktop.NetworkManager.conf
    sed -i 's:deny send_destination:allow send_destination:g' ${S}/callouts/nm-dispatcher.conf
    sed -i 's:deny send_destination:allow send_destination:g' ${S}/callouts/nm-dhcp-client.conf
    sed -i 's:deny send_destination:allow send_destination:g' ${S}/callouts/nm-avahi-autoipd.conf
}

do_install_append () {
    install -d ${D}${sysconfdir}/dbus-1/event.d
    # Additional test binaries
    install -d ${D}${bindir}
    install -m 0755 ${B}/test/.libs/libnm* ${D}${bindir}

    # Install an empty VPN folder as nm-connection-editor will happily segfault without it :o.
    # With or without VPN support built in ;).
    install -d ${D}${sysconfdir}/NetworkManager/VPN

    rm -rf "${D}${localstatedir}/run"
    rmdir --ignore-fail-on-non-empty "${D}${localstatedir}"

    install -d ${D}${datadir}/xenclient/nm_scripts
    install -m 0755 ${WORKDIR}/db_to_nm.awk ${D}${datadir}/xenclient/nm_scripts/db_to_nm.awk
    install -m 0755 ${WORKDIR}/nm_to_db.awk ${D}${datadir}/xenclient/nm_scripts/nm_to_db.awk
    install -m 0755 ${WORKDIR}/WiredEthernetConnection ${D}${datadir}/xenclient/nm_scripts/WiredEthernetConnection
    install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}${datadir}/xenclient/nm_scripts/NetworkManager.conf

    install -m 0755 ${WORKDIR}/nm_sync.sh ${D}${bindir}/nm_sync.sh

    install -d ${D}${nmidldatadir}
    install -m 0644 ${S}/introspection/*.xml ${D}${nmidldatadir}/
}

PACKAGES =+ "libnmutil libnmglib libnmglib-vpn ${PN}-tests ${PN}-bash-completion"

FILES_libnmutil += "${libdir}/libnm-util.so.*"
FILES_libnmglib += "${libdir}/libnm-glib.so.*"
FILES_libnmglib-vpn += "${libdir}/libnm-glib-vpn.so.*"

FILES_${PN} += " \
    ${libexecdir} \
    ${libdir}/pppd/*/nm-pppd-plugin.so \
    ${libdir}/NetworkManager/*.so \
    ${datadir}/polkit-1 \
    ${datadir}/dbus-1 \
    ${base_libdir}/udev/* \
    ${systemd_unitdir}/system/NetworkManager-wait-online.service \
"
FILES_${PN} += " \
    ${datadir}/xenclient \
"

RRECOMMENDS_${PN} += "iptables"
RCONFLICTS_${PN} = "connman"
RDEPENDS_${PN} = " \
    iproute2 \
    dnsmasq \
    wpa-supplicant \
    dhcp-client \
    ${@bb.utils.contains('COMBINED_FEATURES', '3gmodem', 'ppp', '', d)} \
"

FILES_${PN}-dbg += " \
    ${libdir}/NetworkManager/.debug/ \
    ${libdir}/pppd/*/.debug/ \
"

FILES_${PN}-dev += " \
    ${datadir}/NetworkManager/gdb-cmd \
    ${libdir}/pppd/*/*.la \
    ${libdir}/NetworkManager/*.la \
"
FILES_${PN}-dev += " \
    ${nmidldatadir} \
"

FILES_${PN}-tests = " \
    ${bindir}/nm-tool \
    ${bindir}/libnm-glib-test \
    ${bindir}/nm-online \
"

FILES_${PN}-bash-completion = "${datadir}/bash-completion"

SYSTEMD_SERVICE_${PN} = "NetworkManager.service"
