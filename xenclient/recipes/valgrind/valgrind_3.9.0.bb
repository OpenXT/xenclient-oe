DESCRIPTION = "Valgrind memory debugger"
HOMEPAGE = "http://www.valgrind.org/"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYING;md5=c46082167a314d785d012a244748d803"
DEPENDS = "virtual/libx11"
PR = "r0"

##disable CCACHE, valgrind does not like it
CCACHE_pn-valgrind = ""

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
           file://xenclient-4.3-support.patch;striplevel=1"

SRC_URI[md5sum] = "0947de8112f946b9ce64764af7be6df2"
SRC_URI[sha256sum] = "e6af71a06bc2534541b07743e1d58dc3caf744f38205ca3e5b5a0bdf372ed6f0"

S = "${WORKDIR}/valgrind-${PV}"

inherit autotools

EXTRA_OECONF = "--enable-tls --enable-xen"

COMPATIBLE_HOST = "^i.86.*-linux"

FILES_${PN}-dbg += "/usr/lib/valgrind/x86-linux/.debug"
