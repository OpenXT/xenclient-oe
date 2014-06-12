SRC_URI[md5sum] = "486a525eb4827c44b398d6e0a27cd9be"
SRC_URI[sha256sum] = "1dac01908387d0f0b9b4e300d1b6ad244df05a63df73acdd526dc8d4a3096baf"
DESCRIPTION = "x86 real-mode library"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=633af6c02e6f624d4c472d970a2aca53"

PR = "r0"

SRC_URI = "http://archive.debian.org/debian/pool/main/libx/libx86/${PN}_${PV}-1.tar.gz"

S = "${WORKDIR}/libx86-${PV}"

do_configure() {
    sed -i 's/^CFLAGS =/CFLAGSnot =/' Makefile
}

EXTRA_OEMAKE = 'BACKEND=x86emu'

do_compile() {
	oe_runmake BACKEND=x86emu CFLAGS="${CFLAGS} ${LDFLAGS}" shared
	#${STRIP} libx86.so.1
}

do_install() {
	oe_libinstall -so libx86 ${D}${libdir}
	#oe_runmake 'DESTDIR=${D}' install
	#install -D libx86.a ${D}/usr/lib/libx86.a
	install -D lrmi.h ${D}/usr/include/libx86.h
}
