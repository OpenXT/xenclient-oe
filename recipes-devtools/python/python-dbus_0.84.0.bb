DESCRIPTION = "Python bindings for DBus, a socket-based message bus system for interprocess communication"
SECTION = "devel/python"
HOMEPAGE = "http://www.freedesktop.org/Software/dbus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=f5612614133e9a2f2dad527d97554670"
DEPENDS = "expat dbus dbus-glib virtual/libintl python-pyrex-native python-epydoc-native"
PR = "ml2"

SRC_URI = "http://dbus.freedesktop.org/releases/dbus-python/dbus-python-${PV}.tar.gz;name=tarball"
SRC_URI[tarball.md5sum] = "fe69a2613e824463e74f10913708c88a"
SRC_URI[tarball.sha256sum] = "b85bc7aaf1a976627ca461b1ca7b0c4ddddff709f52fe44c9b2d1d7d8fac5906"

S = "${WORKDIR}/dbus-python-${PV}"

inherit distutils-base autotools pkgconfig

export BUILD_SYS
export HOST_SYS

RDEPENDS_${PN} = "\
  python-io \
  python-lang \
  python-logging \
  python-threading \
  python-xml \
"

FILES_${PN}-dev += "\
  ${libdir}/pkgconfig \
"
