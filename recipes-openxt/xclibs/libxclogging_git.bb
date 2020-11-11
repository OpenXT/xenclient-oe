DESCRIPTION = "XenClient Logging Library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

require xclibs.inc

CFLAGS_append = " -Wno-unused"

S = "${WORKDIR}/git/xclogging"

PARALLEL_MAKE = "-j 1"

inherit autotools-brokensep pkgconfig
