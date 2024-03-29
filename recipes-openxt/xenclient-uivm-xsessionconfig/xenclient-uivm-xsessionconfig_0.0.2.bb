SUMMARY = "XSession configurations for UIVM."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://xinitrc \
    file://xfce4-keyboard-shortcuts.xml \
    file://xfwm4.xml \
    file://helpers.rc \
    file://workspaces.xml \
    file://framebuffer.conf \
    file://Xdefaults \
    file://default \
    file://default.keyring \
    file://ui-functions \
    file://nm-applets-agent \
    file://uim-toolbar-gtk-agent \
    file://keymap-agent \
    file://xdg-open \
    file://Xft.xrdb \
    file://xsettings.xml \
    file://gtkrc \
    file://footer_bar_bgslice.png \
    file://xfwm4.tar.gz \
    file://custom-WebBrowser.desktop \
    file://custom-MailReader.desktop \
    file://custom-global.scm \
    file://custom-toolbar.scm \
    file://keyboard \
"

inherit allarch

do_install () {
    install -d ${D}/root/.config/xfce4
    install -m 644 ${WORKDIR}/helpers.rc ${D}/root/.config/xfce4/
    install -m 755 ${WORKDIR}/xinitrc ${D}/root/.config/xfce4/
    install -m 644 ${WORKDIR}/Xft.xrdb ${D}/root/.config/xfce4/

    install -d ${D}/root/.config/xfce4/xfconf/xfce-perchannel-xml
    install -m 644 ${WORKDIR}/xfce4-keyboard-shortcuts.xml ${D}/root/.config/xfce4/xfconf/xfce-perchannel-xml/
    install -m 644 ${WORKDIR}/xfwm4.xml ${D}/root/.config/xfce4/xfconf/xfce-perchannel-xml/
    install -m 644 ${WORKDIR}/xsettings.xml ${D}/root/.config/xfce4/xfconf/xfce-perchannel-xml/

    # Work around https://bugzilla.xfce.org/show_bug.cgi?id=8056
    sed -i 's/&lt;Shift&gt;&lt;Control&gt;/\&lt;Primary\&gt;\&lt;Shift\&gt;/g' ${D}/root/.config/xfce4/xfconf/xfce-perchannel-xml/xfce4-keyboard-shortcuts.xml

    install -d ${D}/root/.config/xfce4/mcs_settings
    install -m 644 ${WORKDIR}/workspaces.xml ${D}/root/.config/xfce4/mcs_settings/

    install -d ${D}/root/.config/xfce4/xfwm4

    install -d ${D}/root/.local/share/xfce4/helpers
    install -m 644 ${WORKDIR}/custom-WebBrowser.desktop ${D}/root/.local/share/xfce4/helpers
    install -m 644 ${WORKDIR}/custom-MailReader.desktop ${D}/root/.local/share/xfce4/helpers

    install -d ${D}${sysconfdir}/modprobe.d
    install -m 644 ${WORKDIR}/framebuffer.conf ${D}${sysconfdir}/modprobe.d/

    install -m 644 ${WORKDIR}/Xdefaults ${D}/root/.Xdefaults

    install -d ${D}/root/.gnome2/keyrings
    install -m 600 ${WORKDIR}/default ${D}/root/.gnome2/keyrings/
    install -m 600 ${WORKDIR}/default.keyring ${D}/root/.gnome2/keyrings/

    install -d ${D}/root/.themes/XenClient/gtk-2.0
    install -m 644 ${WORKDIR}/gtkrc ${D}/root/.themes/XenClient/gtk-2.0/
    install -m 644 ${WORKDIR}/footer_bar_bgslice.png ${D}/root/.themes/XenClient/gtk-2.0/
    install -d ${D}/root/.themes/XenClient/xfwm4
    install -m 644 ${WORKDIR}/xfwm4/* ${D}/root/.themes/XenClient/xfwm4/

    install -d ${D}/root/.uim.d/customs
    install -m 644 ${WORKDIR}/custom-global.scm ${D}/root/.uim.d/customs/
    install -m 644 ${WORKDIR}/custom-toolbar.scm ${D}/root/.uim.d/customs/

    install -d ${D}${libdir}/openxt
    install -m 644 ${WORKDIR}/ui-functions ${D}${libdir}/openxt/ui-functions

    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/nm-applets-agent ${D}${bindir}/
    install -m 755 ${WORKDIR}/uim-toolbar-gtk-agent ${D}${bindir}/
    install -m 755 ${WORKDIR}/keymap-agent ${D}${bindir}/
    install -m 755 ${WORKDIR}/xdg-open ${D}${bindir}/
    install -m 755 ${WORKDIR}/keyboard ${D}${bindir}/
}

FILES_${PN} = " \
    ${sysconfdir} \
    ${bindir} \
    ${libdir} \
    /root/.xfce4/* \
    /root/.config/* \
    /root/.Xdefaults \
    /root/.gnome2/keyrings/* \
    /root/.local \
    /root/.themes \
    /root/.uim.d \
"

RDEPENDS_${PN} += " \
    xrdb \
    bash \
    dbus \
    xen-tools-xenstore \
"
