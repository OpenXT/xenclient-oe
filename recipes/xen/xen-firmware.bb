require xen.inc

SRC_URI += "file://config.patch \
	    file://disable-xen-root-check.patch \
	    file://disable-etherboot.patch \
"

DEPENDS += "seabios ipxe"
DEPENDS += "util-linux"

FILES_${PN} += "/usr/lib/xen/boot/hvmloader"

EXTRA_OEMAKE += "CROSS_SYS_ROOT=${STAGING_DIR_HOST} CROSS_COMPILE=${HOST_PREFIX}"
EXTRA_OEMAKE += "SEABIOS_ROM=${STAGING_DIR_HOST}/usr/share/firmware/bios.bin"
EXTRA_OEMAKE += "PXE_ROM=${STAGING_DIR_HOST}/usr/share/firmware/rtl8139.rom"
EXTRA_OEMAKE += "XENGFX_ROM=${STAGING_DIR_HOST}/usr/share/firmware/vgabios.bin"
EXTRA_OEMAKE += "CONFIG_IOEMU=n"
# Why is that last one necessary?

TARGET_CC_ARCH += "${LDFLAGS}"

do_configure() {
	DESTDIR=${D} ./configure --enable-seabios --prefix=${prefix}
}

do_compile() {
        DESTDIR=${D} oe_runmake -C tools subdir-all-include
        DESTDIR=${D} oe_runmake -C tools subdir-all-firmware
}

do_install() {
        DESTDIR=${D} oe_runmake -C tools subdir-install-include
        DESTDIR=${D} oe_runmake -C tools subdir-install-firmware
}

