SRC_URI[dbus.md5sum] = "8c12fc6af7538c5c8ff12ba683f9d80b"
SRC_URI[dbus.sha256sum] = "4666ec0633dca25887b349f82b51665fa99c56ebf54ad6e560556a08943bc460"
include dbus_1.2.28.inc

PR = "r0xc2"
LICENSE = "GPL-2.0 & AFL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=10dded3b58148f3f1fd804b26354af3e"
DEPENDS += "libselinux libv4v xen-tools"
RDEPENDS += "libselinux"

SRC_URI = "\
  http://dbus.freedesktop.org/releases/dbus/dbus-${PV}.tar.gz;name=dbus \
  file://tmpdir.patch;patch=1 \
  file://fix-install-daemon.patch;patch=1 \
  file://0001-Make-the-default-DBus-reply-timeout-configurable.patch;patch=1 \
  file://add-domid-authentication.patch;patch=1 \
  file://v4v.patch;patch=1 \
  file://system.conf \
  file://dbus-1.init \
"

do_install_append() {
	install -m 0755 -d ${D}/etc
	install -m 0755 -d ${D}/etc/dbus-1
	install -m 0644 ${WORKDIR}/system.conf ${D}/etc/dbus-1
}
