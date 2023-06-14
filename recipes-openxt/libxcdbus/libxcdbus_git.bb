DESCRIPTION = "XenClient DBUS library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = " \
    libtool \
    libevent \
"

PV = "0+git${SRCPV}"

SRCREV = "3343da2c4f32958b7e81a54fad21f0f4d0170302"
SRC_URI = "git://github.com/OpenXT/libxcdbus.git;protocol=https"

S = "${WORKDIR}/git"

inherit autotools-brokensep pkgconfig lib_package xc-rpcgen-c
