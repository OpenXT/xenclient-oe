require xen.inc

inherit pkgconfig pythonnative

DEPENDS += "util-linux xen-tools openssl libaio libicbinn-resolved"

RDEPENDS_${PN} += "glibc-gconv-utf-16"

EXTRA_OEMAKE += "CROSS_SYS_ROOT=${STAGING_DIR_HOST} CROSS_COMPILE=${HOST_PREFIX}"
EXTRA_OEMAKE += "CONFIG_IOEMU=n"
EXTRA_OEMAKE += "DESTDIR=${D}"

TARGET_CC_ARCH += "${LDFLAGS}"

do_configure() {
	DESTDIR=${D} ./configure --prefix=${prefix}
}

do_compile() {
        oe_runmake -C tools subdir-all-blktap2
}

do_install() {
        oe_runmake DESTDIR=${D} -C tools subdir-install-blktap2
        install -d ${D}/usr/include
        install tools/blktap2/control/tap-ctl.h ${D}/usr/include
        install tools/blktap2/include/tapdisk-message.h ${D}/usr/include	
}
