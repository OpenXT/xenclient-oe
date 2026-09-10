DESCRIPTION = "XenClient Logging Library"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

require xclibs.inc

CFLAGS:append = " -Wno-unused"

S = "${UNPACKDIR}/${BP}/xclogging"

PARALLEL_MAKE = "-j 1"

inherit autotools-brokensep pkgconfig
