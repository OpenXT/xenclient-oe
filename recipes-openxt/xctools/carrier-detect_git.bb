DESCRIPTION = "network interface carrier detect program"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libnl"

require xctools.inc

S = "${UNPACKDIR}/${BP}/carrier-detect"

inherit autotools pkgconfig
