DESCRIPTION = "Midori is a lightweight web browser."
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"

DEPENDS = "webkit-gtk libsoup-2.4 libsexy" 

inherit autotools-brokensep gtk-icon-cache pkgconfig

SRC_URI = "http://software.twotoasts.de/media/midori/midori-${PV}.tar.gz"

EXTRA_OECONF = "  --disable-hildon "

TARGET_CC_ARCH += "${LDFLAGS}"
