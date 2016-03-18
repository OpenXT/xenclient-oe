
SRC_URI = "file://xinitrc \
	   file://xfce4-keyboard-shortcuts.xml \
	   file://xfwm4.xml \
	   file://helpers.rc \
           file://workspaces.xml \
	   file://session.xbel \
	   file://config \
	   file://framebuffer.conf \
	   file://Xdefaults \
	   file://default \
	   file://default.keyring \
           file://midori_login.sh \
           file://midori_report.sh \
           file://nm-applet-wrapper \
           file://nm-applet-launcher \
           file://start-nm-applet \
           file://v4v.modutils \
           file://xenfb2.modutils \
           file://xenkbd.modutils \
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
           file://get-language \
           file://uim-toolbar-gtk-wrapper \
           file://language-sync \
           file://nm-backend-sync \
           file://keyboard \
"

RDEPENDS_${PN} += "xdotool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PR = "r26"

FILES_${PN} = "/etc/* /root/.xfce4/* /root/.config/* /root/.Xdefaults /root/.gnome2/keyrings/* /root/.local /root/.themes /root/.uim.d /usr/bin"

do_install () {
	   install -d ${D}/root/.xfce4
	   install -m 644 ${WORKDIR}/helpers.rc ${D}/root/.xfce4/

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

	   install -d ${D}/root/.local/share/xfce4/helpers
	   install -m 644 ${WORKDIR}/custom-WebBrowser.desktop ${D}/root/.local/share/xfce4/helpers
	   install -m 644 ${WORKDIR}/custom-MailReader.desktop ${D}/root/.local/share/xfce4/helpers

	   install -d ${D}/root/.config/midori
	   install -m 644 ${WORKDIR}/session.xbel ${D}/root/.config/midori/
	   install -m 644 ${WORKDIR}/config ${D}/root/.config/midori/

	   install -d ${D}/etc/modprobe.d
	   install -m 644 ${WORKDIR}/framebuffer.conf ${D}/etc/modprobe.d/

           install -d ${D}/etc/modutils
	   install -m 644 ${WORKDIR}/v4v.modutils ${D}/etc/modutils
	   install -m 644 ${WORKDIR}/xenfb2.modutils ${D}/etc/modutils
	   install -m 644 ${WORKDIR}/xenkbd.modutils ${D}/etc/modutils

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

           install -d ${D}/usr/bin
           install -m 755 ${WORKDIR}/midori_login.sh ${D}/usr/bin/
           install -m 755 ${WORKDIR}/midori_report.sh ${D}/usr/bin/
           install -m 755 ${WORKDIR}/nm-applet-wrapper ${D}/usr/bin/
           install -m 755 ${WORKDIR}/nm-applet-launcher ${D}/usr/bin/
           install -m 755 ${WORKDIR}/start-nm-applet ${D}/usr/bin/
           install -m 755 ${WORKDIR}/xdg-open ${D}/usr/bin/
           install -m 755 ${WORKDIR}/get-language ${D}/usr/bin/
           install -m 755 ${WORKDIR}/uim-toolbar-gtk-wrapper ${D}/usr/bin/
           install -m 755 ${WORKDIR}/language-sync ${D}/usr/bin/
           install -m 755 ${WORKDIR}/nm-backend-sync ${D}/usr/bin/
           install -m 755 ${WORKDIR}/keyboard ${D}/usr/bin/
}
