# syslinux OE build file
# Copyright (C) 2009, O.S. Systems Software Ltda.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION = "A multi-purpose linux bootloader"
HOMEPAGE = "http://syslinux.zytor.com/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"
DEPENDS = "nasm-native"
RRECOMMENDS_${PN} = "mtools"
PR = "r0xc0"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/boot/syslinux/syslinux-${PV}.tar.bz2 \
           file://avoid-using-kernel-ext2-header.patch;patch=1 \
"
SRC_URI[md5sum] = "a3936208767eb7ced65320abe2e33a10"
SRC_URI[sha256sum] = "e186a21cb1b3b1874f253df21546e8d0595d803bd6a60f38cfafbc10bee90e0b"

TARGET_CC_ARCH += "${LDFLAGS}"

EXTRA_OEMAKE = " \
	BINDIR=${bindir} SBINDIR=${base_sbindir} LIBDIR=${libdir} \
	DATADIR=${datadir} MANDIR=${mandir} INCDIR=${includedir} \
"

do_configure() {
	# drop win32 targets or build fails
	sed -e 's,win32/\S*,,g' -i Makefile

	# clean installer executables included in source tarball
	oe_runmake clean
}

do_compile() {
	# Rebuild only the installer; keep precompiled bootloaders
	# as per author's request (doc/distrib.txt)
	oe_runmake CC="${CC}" installer
}

do_install() {
	oe_runmake install INSTALLROOT="${D}"
}

PACKAGES =+ " \
	${PN}-extlinux \
	${PN}-isohybrid \
	${PN}-isolinux \
	${PN}-mboot \
	${PN}-mbr \
	${PN}-pxelinux \
"

FILES_${PN} = "${bindir}/syslinux"
FILES_${PN}-extlinux = "${base_sbindir}/extlinux"
FILES_${PN}-isohybrid = "${bindir}/isohybrid"
FILES_${PN}-isolinux = "${datadir}/${PN}/isolinux.bin"
FILES_${PN}-mboot = "${datadir}/${PN}/mboot.c32"
FILES_${PN}-mbr = "${datadir}/${PN}/mbr.bin"
FILES_${PN}-pxelinux = "${datadir}/${PN}/pxelinux.0"
FILES_${PN}-dev += "${datadir}/${PN}/com32"
