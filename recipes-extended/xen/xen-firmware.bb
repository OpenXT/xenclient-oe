require xen.inc

SRC_URI += "file://config.patch \
	    file://disable-xen-root-check.patch \
	    file://disable-etherboot.patch \
	    file://acpi-pm-feature.patch \
"

DEPENDS += "\
    seabios \
    ipxe \
    util-linux \
    vgabios \
    iasl-native \
"
PR = "${INC_PR}.1"

FILES_${PN} += "/usr/lib/xen/boot/hvmloader"

EXTRA_OEMAKE += "DESTDIR=${D}"
EXTRA_OEMAKE += "CROSS_SYS_ROOT=${STAGING_DIR_HOST} CROSS_COMPILE=${HOST_PREFIX}"
EXTRA_OEMAKE += "SEABIOS_ROM=${STAGING_DIR_HOST}/usr/share/firmware/bios.bin"
EXTRA_OEMAKE += "PXE_ROM=${STAGING_DIR_HOST}/usr/share/firmware/rtl8139.rom"
EXTRA_OEMAKE += "STDVGA_ROM=${STAGING_DIR_HOST}/usr/share/firmware/vgabios-0.7a.bin"
EXTRA_OEMAKE += "CIRRUSVGA_ROM=${STAGING_DIR_HOST}/usr/share/firmware/vgabios-0.7a.cirrus.bin"
EXTRA_OEMAKE += "CONFIG_IOEMU=n"
# Why is that last one necessary?

TARGET_CC_ARCH += "${LDFLAGS}"

inherit pythonnative

do_configure() {
	DESTDIR=${D} ./configure --enable-seabios --disable-rombios --prefix=${prefix}
}

do_compile() {
        oe_runmake -C tools subdir-all-include
        oe_runmake -C tools subdir-all-firmware
}

do_install() {
        oe_runmake -C tools subdir-install-include
        oe_runmake -C tools subdir-install-firmware

	# Remove files already provided by xen-tools
	rm -rf ${D}/usr/include
}

