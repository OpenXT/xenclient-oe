DESCRIPTION = "XenClient DBUS library"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " \
    libtool \
    libevent \
"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/libxcdbus.git;protocol=https;branch=master"


inherit autotools-brokensep pkgconfig lib_package xc-rpcgen-c
