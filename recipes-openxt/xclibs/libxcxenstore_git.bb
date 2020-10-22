DESCRIPTION = "Xenstore library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen libxclogging libevent"

require xclibs.inc

S = "${WORKDIR}/git/xcxenstore"

ASNEEDED = ""

inherit autotools-brokensep pkgconfig
