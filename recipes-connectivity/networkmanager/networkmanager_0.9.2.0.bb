DESCRIPTION = "NetworkManager"
SECTION = "net/misc"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=cbbffd568227ada506640fe950a4823b"

DEPENDS += " \
    libnl \
    dbus \
    dbus-glib \
    dbus-glib-native \
    udev \
    wireless-tools \
    nss \
    gnutls \
    util-linux \
    ppp \
    intltool-native \
    libgudev \
"

# gnomebase.bbclass strongly assign SRC_URI.
GNOME_COMPRESS_TYPE = "bz2"
GNOMEBN = "NetworkManager"
inherit gnome

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"
SRC_URI += " \
    file://0001-don-t-try-to-run-sbin-dhclient-to-get-the-version-nu.patch \
    file://use-bridge-iface.patch \
    file://xc-nutty-network.patch \
    file://dont-reset-wired-mac-addr.patch \
    file://libnl3-routes-cache-args.patch \
    file://only-request-secrets-on-initial-connect.patch \
    file://auto-activate-connections.patch \
    file://linux3-compile.patch \
    file://remove-libgcrypt.patch \
    file://gtk-doc.make \
    file://NetworkManager.conf \
    file://ac-wireless \
    file://01ppp \
"
SRC_URI[archive.md5sum] = "bc0b00b8a187762d93c50a9706b4c5c3"
SRC_URI[archive.sha256sum] = "a178ed2f0b5a1045ec47b217ea531d0feba9208f6bcfe64b701174a5c1479816"

SRC_URI += " \
    file://db-nm-settings.patch \
    file://use-dom0-db-for-seen-bssids.patch \
    file://WiredEthernetConnection \
    file://nm_sync.sh \
    file://db_to_nm.awk \
    file://nm_to_db.awk \
"

S = "${WORKDIR}/NetworkManager-${PV}"

inherit gettext gobject-introspection

export GIR_EXTRA_LIBS_PATH="${B}/libnm-util/.libs:${B}/libnm-glib/.libs"

EXTRA_OECONF += " \
    --with-distro=debian \
    --with-crypto=gnutls \
    --disable-more-warnings \
    --with-dhclient=${base_sbindir}/dhclient \
    --with-iptables=${sbindir}/iptables \
    --with-tests \
    --with-systemdsystemunitdir=no \
    --enable-polkit=no \
    --with-ck=no \
    --enable-wimax=no \
"
# disable-schemas-install does not exist yet.
EXTRA_OECONF_remove += "--disable-schemas-install"
CFLAGS_append += "-Wno-deprecated-declarations"

do_configure_prepend() {
    cp ${WORKDIR}/gtk-doc.make ${S}/
    echo "EXTRA_DIST = version.xml" > gnome-doc-utils.make
    sed -i -e 's:man \\:man:' -e s:docs::g ${S}/Makefile.am
    sed -i -e /^docs/d ${S}/configure.ac
}

# Work around dbus permission problems since we lack a proper at_console
do_install_prepend() {
	sed -i -e s:deny:allow:g ${S}/src/NetworkManager.conf
	sed -i -e s:deny:allow:g ${S}/callouts/nm-dispatcher.conf
}

do_install_append () {
    install -m 0755 -d ${D}${sysconfdir}/dbus-1
    install -m 0755 -d ${D}${sysconfdir}/dbus-1/event.d
    # Additional test binaries
    install -d ${D}${bindir}
    install -m 0755 ${B}/test/.libs/libnm* ${D}${bindir}
    
    # Install an empty VPN folder as nm-connection-editor will happily segfault without it :o.
    # With or without VPN support built in ;).
    install -m 0755 -d ${D}${sysconfdir}/NetworkManager
    install -m 0755 -d ${D}${sysconfdir}/NetworkManager/VPN
    
    rm -f ${D}${sysconfdir}/init.d/NetworkManager
    install -m 0755 -d ${D}${nmidldatadir}
    install -m 0644 ${S}/introspection/*.xml ${D}${nmidldatadir}
    
    install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}/etc/NetworkManager
    install -d ${D}/etc/acpi/actions
    install -m 0755 ${WORKDIR}/ac-wireless ${D}/etc/acpi/actions/ac-wireless
    install -m 0755 ${WORKDIR}/01ppp ${D}/etc/NetworkManager/dispatcher.d

    # OpenXT additional scripts for NDVM.
    install -m 0755 ${WORKDIR}/nm_sync.sh ${D}${bindir}/nm_sync.sh
    install -m 0755 -d ${D}${datadir}/xenclient/nm_scripts
    install -m 0755 ${WORKDIR}/db_to_nm.awk ${D}${datadir}/xenclient/nm_scripts/db_to_nm.awk
    install -m 0755 ${WORKDIR}/nm_to_db.awk ${D}${datadir}/xenclient/nm_scripts/nm_to_db.awk
    install -m 0755 ${WORKDIR}/WiredEthernetConnection ${D}${datadir}/xenclient/nm_scripts/WiredEthernetConnection
    install -m 0644 ${D}${sysconfdir}/NetworkManager/NetworkManager.conf ${D}${datadir}/xenclient/nm_scripts/
}

SYSTEMD_PACKAGES = "${PN}"
#SYSTEMD_SERVICE = "NetworkManager.service"
SYSTEMD_SERVICE = ""

PACKAGES =+ " \
    libnmutil \
    libnmglib \
    libnmglib-vpn \
    ${PN}-tests \
"

FILES_libnmutil += "${libdir}/libnm-util.so.*"
FILES_libnmglib += "${libdir}/libnm_glib.so.*"
FILES_libnmglib-vpn += "${libdir}/libnm_glib_vpn.so.*"

FILES_${PN} += " \
    ${libexecdir} \
    ${libdir}/pppd/*/nm-pppd-plugin.so \
    ${libdir}/NetworkManager/*.so \
    ${datadir}/polkit-1 \
    ${datadir}/dbus-1 \
    ${base_libdir}/udev/* \
    ${systemd_unitdir}/system/NetworkManager-wait-online.service \
    ${nmidldatadir} \
"
FILES_${PN} += " \
    ${datadir}/xenclient/nm_scripts/db_to_nm.awk \
    ${datadir}/xenclient/nm_scripts/nm_to_db.awk \
    ${datadir}/xenclient/nm_scripts/WiredEthernetConnection \
    ${datadir}/xenclient/nm_scripts/NetworkManager.conf \
    ${bindir}/nm_sync.sh \
"

RRECOMMENDS_${PN} += "iptables"
RCONFLICTS_${PN} = "connman"
RDEPENDS_${PN} = " \
    wpa-supplicant \
    dhcp-client \
    ${@bb.utils.contains('COMBINED_FEATURES', '3gmodem', 'ppp', '', d)} \
    libgudev \
    wireless-tools \
    dnsmasq \
    iproute2 \
    networkmanager-certs \
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
FILES_${PN}-tests = " \
    ${bindir}/nm-tool \
    ${bindir}/libnm_glib_test \
    ${bindir}/nm-online \
"
