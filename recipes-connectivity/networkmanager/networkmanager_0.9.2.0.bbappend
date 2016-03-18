PR .= ".1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# add SE Linux dependency, so selinux is detected and pam selinux module is build
# unfortunately there is no way to enforce failure when libselinux is not present
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
DEPENDS = "libnl dbus dbus-glib udev wireless-tools gnutls util-linux ppp"
RDEPENDS_${PN} += "libgudev wireless-tools dnsmasq iproute2 networkmanager-certs" 

SRC_URI += " \
            file://use-bridge-iface.patch;patch=1 \
            file://xc-nutty-network.patch;patch=1 \
            file://dont-reset-wired-mac-addr.patch;patch=1 \
            file://libnl3-routes-cache-args.patch;patch=1 \
            file://only-request-secrets-on-initial-connect.patch;patch=1 \
            file://auto-activate-connections.patch;patch=1 \
            file://linux3-compile.patch;patch=1 \
            file://NetworkManager.conf \
            file://ac-wireless \
            file://01ppp \
            file://remove-libgcrypt.patch \
"

EXTRA_OECONF += " \
                 --with-systemdsystemunitdir=no \
                 --enable-polkit=no \
                 --with-ck=no \
                 --enable-wimax=no \
"

CFLAGS_append += " -Wno-deprecated-declarations "

do_install_append () {
        rm -f ${D}/etc/init.d/NetworkManager
        install -m 0755 -d ${D}/usr/share/nm-idl
        install -m 0644 ${S}/introspection/*.xml ${D}/usr/share/nm-idl/

        install -m 0644 ${WORKDIR}/NetworkManager.conf ${D}/etc/NetworkManager/
        install -d ${D}/etc/acpi/actions
        install -m 0755 ${WORKDIR}/ac-wireless ${D}/etc/acpi/actions/ac-wireless
        install -m 0755 ${WORKDIR}/01ppp ${D}/etc/NetworkManager/dispatcher.d

}
FILES_${PN}_append_xenclient-ndvm += " \
                 /usr/share/xenclient/nm_scripts/db_to_nm.awk \
                 /usr/share/xenclient/nm_scripts/nm_to_db.awk \
                 /usr/share/xenclient/nm_scripts/WiredEthernetConnection \
                 /usr/share/xenclient/nm_scripts/NetworkManager.conf \
                 /usr/bin/nm_sync.sh \
"

SRC_URI_append_xenclient-ndvm += " \
            file://db-nm-settings.patch;patch=1 \
            file://use-dom0-db-for-seen-bssids.patch;patch=1 \
            file://WiredEthernetConnection \
            file://nm_sync.sh \
            file://db_to_nm.awk \
            file://nm_to_db.awk \
"

B = "${S}"

do_install_append_xenclient-ndvm() {
        install -m 0755 ${WORKDIR}/nm_sync.sh ${D}/usr/bin/nm_sync.sh
        install -d ${D}/usr/share/xenclient/nm_scripts
        install -m 0755 ${WORKDIR}/db_to_nm.awk ${D}/usr/share/xenclient/nm_scripts/db_to_nm.awk
        install -m 0755 ${WORKDIR}/nm_to_db.awk ${D}/usr/share/xenclient/nm_scripts/nm_to_db.awk
        install -m 0755 ${WORKDIR}/WiredEthernetConnection ${D}/usr/share/xenclient/nm_scripts/WiredEthernetConnection
        install -m 0644 ${D}/etc/NetworkManager/NetworkManager.conf ${D}/usr/share/xenclient/nm_scripts/
}

SYSTEMD_SERVICE = ""
FILES_libnmglib += "${libdir}/libnm-glib.so.*"
FILES_libnmglib-vpn += "${libdir}/libnm-glib-vpn.so.*"

