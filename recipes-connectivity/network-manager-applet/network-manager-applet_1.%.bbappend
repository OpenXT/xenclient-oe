FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

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
    file://nm-signal-00.png \
    file://nm-signal-25.png \
    file://nm-signal-50.png \
    file://nm-signal-75.png \
    file://nm-signal-100.png \
    file://xc-menus.patch \
    file://disable_available_to_all_users_checkbox.patch \
    file://default-certs-dir.patch \
    file://always-use-psk-hash.patch \
    file://disable-show-password.patch \
"

FILES_${PN} += " \
    ${datadir}/nm-applet/ \
    ${datadir}/libnm-gtk/wifi.ui \
    ${datadir}/gnome-vpn-properties/ \
    ${datadir}/gnome/autostart/ \
"
FILES_${PN} += "${libdir}/gnome-bluetooth/plugins/*.so"
FILES_${PN}-dev += "${libdir}/gnome-bluetooth/plugins/libnma.la"
FILES_${PN}-staticdev += "${libdir}/gnome-bluetooth/plugins/libnma.a"
FILES_${PN}-dbg += "${libdir}/gnome-bluetooth/plugins/.debug/"

inherit gnome

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

RDEPENDS_${PN} =+ "networkmanager"
RRECOMMENDS_${PN} =+ "gnome-bluetooth gnome-keyring"
