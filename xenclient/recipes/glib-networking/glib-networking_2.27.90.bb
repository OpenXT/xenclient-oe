DESCRIPTION = "glib-networking contains the implementations of certain GLib networking features that cannot be implemented directly in GLib itself because of their dependencies."
HOMEPAGE = "http://git.gnome.org/browse/glib-networking/"
BUGTRACKER = "http://bugzilla.gnome.org"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

SECTION = "libs"
DEPENDS = "glib-2.0 gnutls"

PR = "r0"

SRC_URI = "${GNOME_MIRROR}/${BPN}/2.27/${BPN}-${PV}.tar.bz2;name=glib-networking \
           file://glib-networking-work-with-autoconf-2.63.patch;patch=1 \
"

SRC_URI[glib-networking.sha256sum] = "828b7d13d14f401b849bde5c31e92e7ecd7c7db9c4ce4ec47b7025fb63600469"

EXTRA_OECONF = "--without-ca-certificates"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/gio/modules/libgio* ${datadir}/dbus-1/services/"
FILES_${PN}-dbg += "${libdir}/gio/modules/.debug/"
