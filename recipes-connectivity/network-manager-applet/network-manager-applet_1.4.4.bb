SUMMARY = "GTK+ applet for NetworkManager"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

DEPENDS = "gtk+3 libnotify libsecret networkmanager \
           gconf libgnome-keyring iso-codes nss \
           intltool-native \
"

inherit gnomebase gsettings gtk-icon-cache gobject-introspection

GNOME_COMPRESS_TYPE = "xz"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"
SRC_URI += " \
    file://openxt-menus.patch \
    file://disable-show-password.patch \
    file://disable-available-to-all-users-checkbox.patch \
    file://always-use-psk-hash.patch \
    file://default-certs-dir.patch \
    file://disable-auto-ethernet.patch \
"

SRC_URI += " \
    file://org.openxt.nmapplet.xml \
"

S = "${WORKDIR}/${BPN}-${PV}"

SRC_URI[archive.md5sum] = "3f82cedc4703df0277c76d9feb5bf2c8"
SRC_URI[archive.sha256sum] = "693846eeae0986e79eb1cedfbc499f132f27a9976ef189a0f16938ac59ec3226"

PACKAGECONFIG[modemmanager] = "--with-wwan,--without-wwan,modemmanager"
PACKAGECONFIG ??= ""

do_configure_append() {
    # Sigh... --enable-compile-warnings=no doesn't actually turn off -Werror
    for i in $(find ${B} -name "Makefile") ; do
        sed -i -e s:-Werror::g $i
    done
    gdbus-codegen --generate-c-code ${S}/src/popup-menu --c-namespace OpenXT --interface-prefix org.openxt. ${WORKDIR}/org.openxt.nmapplet.xml
}

RDEPENDS_${PN} =+ "networkmanager"

FILES_${PN} += " \
    ${datadir}/appdata \
    ${datadir}/nm-applet/ \
    ${datadir}/libnm-gtk/wifi.ui \
    ${datadir}/libnma/wifi.ui \
"
