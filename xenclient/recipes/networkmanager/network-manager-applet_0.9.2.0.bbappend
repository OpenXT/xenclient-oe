PRINC = "1"
FILESEXTRAPATHS := "${THISDIR}/${PN}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "polkit-gnome libnotify networkmanager dbus-glib libglade gconf gnome-keyring libgnome-keyring iso-codes"

#RDEPENDS = "${@base_conditional('MACHINE', 'xenclient-uivm', '', 'networkmanager', d)} notification-daemon"

RDEPENDS_${PN} = "libnmglib libnmutil libnmglib-vpn gnome-keyring networkmanager-certs"
RRECOMMENDS_${PN} = ""

SRC_URI += " \
           file://075_Adhoc_h32bit_22x22.png \
           file://075_NetworkNoBars_h32bit_22x22.png \
           file://075_NoConnection_h32bit_22x22.png \
           file://075_Wired_h32bit_22x22.png \
           file://075_WWan_h32bit_22x22.png \
           file://075_Adhoc_h32bit_50x50.png \
           file://075_Loading1_h32bit_50x50.png \
           file://075_Loading2_h32bit_50x50.png \
           file://075_Loading3_h32bit_50x50.png \
           file://075_Loading4_h32bit_50x50.png \
           file://075_Loading5_h32bit_50x50.png \
           file://075_Loading6_h32bit_50x50.png \
           file://075_Loading7_h32bit_50x50.png \
           file://075_Loading8_h32bit_50x50.png \
           file://075_Network1Bar_h32bit_50x50.png \
           file://075_Network2Bars_h32bit_50x50.png \
           file://075_Network3Bars_h32bit_50x50.png \
           file://075_Network4Bars_h32bit_50x50.png \
           file://075_NetworkNoBars_h32bit_50x50.png \
           file://075_NoConnection_h32bit_50x50.png \
           file://075_Wired_h32bit_50x50.png \
           file://075_WWan_h32bit_50x50.png \
           file://xc-menus.patch;patch=1 \
           file://disable_available_to_all_users_checkbox.patch;patch=1 \
           file://default-certs-dir.patch;patch=1 \
"

EXTRA_OECONF += " \
                  --with-bluetooth=no \
"
PACKAGE_ARCH_xenclient-uivm = "${MACHINE_ARCH}"

do_install_append() {
        install -d ${D}${datadir}/icons/hicolor/22x22/xenclient

        install -m 0644 ${WORKDIR}/075_Adhoc_h32bit_22x22.png \
                        ${WORKDIR}/075_NetworkNoBars_h32bit_22x22.png \
                        ${WORKDIR}/075_NoConnection_h32bit_22x22.png \
                        ${WORKDIR}/075_Wired_h32bit_22x22.png \
                        ${WORKDIR}/075_WWan_h32bit_22x22.png \
                        ${D}${datadir}/icons/hicolor/22x22/xenclient

        install -d ${D}${datadir}/icons/hicolor/50x50/xenclient

        install -m 0644 ${WORKDIR}/075_Adhoc_h32bit_50x50.png \
                        ${WORKDIR}/075_Loading1_h32bit_50x50.png \
                        ${WORKDIR}/075_Loading2_h32bit_50x50.png \
                        ${WORKDIR}/075_Loading3_h32bit_50x50.png \
                        ${WORKDIR}/075_Loading4_h32bit_50x50.png \
                        ${WORKDIR}/075_Loading5_h32bit_50x50.png \
                        ${WORKDIR}/075_Loading6_h32bit_50x50.png \
                        ${WORKDIR}/075_Loading7_h32bit_50x50.png \
                        ${WORKDIR}/075_Loading8_h32bit_50x50.png \
                        ${WORKDIR}/075_Network1Bar_h32bit_50x50.png \
                        ${WORKDIR}/075_Network2Bars_h32bit_50x50.png \
                        ${WORKDIR}/075_Network3Bars_h32bit_50x50.png \
                        ${WORKDIR}/075_Network4Bars_h32bit_50x50.png \
                        ${WORKDIR}/075_NetworkNoBars_h32bit_50x50.png \
                        ${WORKDIR}/075_NoConnection_h32bit_50x50.png \
                        ${WORKDIR}/075_Wired_h32bit_50x50.png \
                        ${WORKDIR}/075_WWan_h32bit_50x50.png \
                        ${D}${datadir}/icons/hicolor/50x50/xenclient
}

