PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

#RDEPENDS_${PN} = "${@base_conditional('MACHINE', 'xenclient-uivm', '', 'networkmanager', d)} notification-daemon"

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
           file://nm-signal-00.png \
           file://nm-signal-25.png \
           file://nm-signal-50.png \
           file://nm-signal-75.png \
           file://nm-signal-100.png \
"

SRC_URI[archive.md5sum] = "9623aeb6c782a8d782500cf12c887b5b"
SRC_URI[archive.sha256sum] = "ebe725d0140f658c6a3f384674c72fba7a7c417df3be0e84ee8f45e6dfc219de"

EXTRA_OECONF += " \
                  --with-bluetooth=no \
"
PACKAGE_ARCH_xenclient-uivm = "${MACHINE_ARCH}"

CFLAGS_append += " -Wno-error=unused-but-set-variable -Wno-deprecated-declarations -Wno-unused-function -Wno-error=declaration-after-statement"

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

        # The old nm-applet package we used had some bad png files that now make
        # other components choke.  Pulled these from a newer version. 
	rm -f ${D}${datadir}/icons/hicolor/22x22/apps/nm-signal-*.png
        install -m 0644 ${WORKDIR}/nm-signal-00.png \
                        ${WORKDIR}/nm-signal-25.png \
                        ${WORKDIR}/nm-signal-50.png \
                        ${WORKDIR}/nm-signal-75.png \
                        ${WORKDIR}/nm-signal-100.png \
                        ${D}${datadir}/icons/hicolor/22x22/apps
}

