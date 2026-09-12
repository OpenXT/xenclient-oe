DESCRIPTION = "Xenstore library"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen-tools libxclogging libevent"

require xclibs.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://0001-do-not-build-sample.patch"

S = "${UNPACKDIR}/${BP}/xcxenstore"

ASNEEDED = ""

inherit autotools-brokensep pkgconfig

# sample/teststore.c uses implicit declarations; GCC 15 errors on it.
EXTRA_OEMAKE += "SUBDIRS=src"
