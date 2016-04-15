DESCRIPTION = "Trusted Boot"
HOMEPAGE = "http://tboot.sourceforge.net/"
SECTION = "bootloaders"
PRIORITY = "optional"
DEPENDS = "trousers"

EXTRA_OEMAKE += "TARGET_ARCH=x86_32 CROSS_COMPILE=${TARGET_PREFIX} INSTALL_STRIP="

do_compile() {
	oe_runmake SUBDIRS:="tboot" CC="${HOST_PREFIX}gcc ${TOOLCHAIN_OPTIONS}" CPP="${HOST_PREFIX}cpp ${TOOLCHAIN_OPTIONS}" LDFLAGS="" CFLAGS=""
	oe_runmake -C ${S}/tb_polgen CFLAGS+="-std=c99"
	oe_runmake -C ${S}/lcptools CFLAGS+="-std=c99"
	oe_runmake CFLAGS+=" -std=c99 "
	${STRIP} ${S}/tb_polgen/tb_polgen
}

do_install() {
	install -d ${D}/boot
	install -m 444 tboot/tboot.gz ${D}/boot/
        install -d ${D}/usr/share/xenclient
	oe_runmake -C ${S}/tb_polgen install DISTDIR=${D} 
	oe_runmake -C ${S}/lcptools install DISTDIR=${D}
	oe_runmake -C ${S}/utils install DISTDIR=${D}
}

FILES_${PN}-dbg += " /boot/tboot-syms "

FILES_${PN} += " \
        /boot/tboot.gz \
        /usr/sbin/txt-stat \
        /usr/sbin/parse_err \
        /usr/share/xenclient \
"