SRC_URI[md5sum] = "fba8f4901f12370ad6df8725d840a33b"
SRC_URI[sha256sum] = "55a1e667249b4b38b6d48e74950c1dadd4d8b9802e358956fd4050508aae12d3"
DESCRIPTION = "vbetool for XenClient"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=48a5edcd17b7ae645f03eef18cd5e540"
DEPENDS = "libx86 pciutils"

PR = "r3"

inherit autotools-brokensep

SRC_URI = "${DEBIAN_MIRROR}/main/v/vbetool/vbetool_${PV}.orig.tar.gz"

S = "${WORKDIR}/vbetool-${PV}"

ASNEEDED = ""

# vbetool's autoconf is seriously underpowered and it's much better to avoid it
# so we compile it by hand instead
do_configure() {
	:
}

do_compile() {
	${CC} -c ${CFLAGS} ${LDFLAGS} vbetool.c
	${CC} ${LDFLAGS} -lx86 -lpci -o vbetool vbetool.o
	${STRIP} vbetool
}

do_install () {
           install -d ${D}/usr/bin
	   install -m 755 vbetool ${D}/usr/bin/
}
