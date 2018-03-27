DESCRIPTION = "GTK+ applet for NetworkManager" 
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

DEPENDS = " \
    libnotify \
    networkmanager \
    dbus-glib \
    dbus-glib-native \
    libglade \
    gconf \
    gnome-keyring \
    libgnome-keyring \
    iso-codes \
    openssl \
    intltool-native \
    polkit \
"

GNOME_COMPRESS_TYPE = "bz2"
inherit gnome gtk-icon-cache gobject-introspection

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"
SRC_URI += " \
    file://xc-menus.patch \
    file://disable_available_to_all_users_checkbox.patch \
    file://default-certs-dir.patch \
    file://always-use-psk-hash.patch \
    file://disable-show-password.patch \
"
SRC_URI[archive.md5sum] = "feaf2c8427d23924dde7de52ff4c5078"
SRC_URI[archive.sha256sum] = "287301692224cc1bb20abe8bc52140461f565e58898a99daef11a188bb29b362"

# GTK2.x mode
EXTRA_OECONF += " \
    --with-gtkver=2 \
    --with-bluetooth=no \
"

LDFLAGS_append += "-lcrypto"
CFLAGS_append += " \
    -Wno-error=unused-but-set-variable \
    -Wno-deprecated-declarations \
    -Wno-unused-function \
    -Wno-error=declaration-after-statement \
    -Wno-error=unused-const-variable \
"

do_configure_append() {
        rm config.log
        # Sigh... --enable-compile-warnings=no doesn't actually turn off -Werror
        for i in $(find ${S} -name "Makefile") ; do
            sed -i -e s:-Werror::g $i
        done
}

RDEPENDS_${PN} =+ "networkmanager"
RRECOMMENDS_${PN} =+ "gnome-bluetooth gnome-keyring"

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
