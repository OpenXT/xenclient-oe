DESCRIPTION = "IPXE"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"

PV = "0+git${SRCPV}"

SRCREV_source = "8d038040eaac85bbe08f0b5ba507ff0167b3a2f3"

SRC_URI = "git://git.ipxe.org/ipxe.git;protocol=git;name=source \
    file://early-debug.patch;patch=1 \
    file://xen-debug-output-driver.patch;patch=1 \
    file://fix-compile-gcc-7.4.2.patch;patch=1 \
"

S = "${WORKDIR}/git"

inherit xenclient

FILES_${PN} = "/usr/share/firmware/*.rom"

PARALLEL_MAKE=""
# Use host toolchain. God please forgive me.
EXTRA_OEMAKE += "CFLAGS= LDFLAGS= CC='${BUILD_CC}' LD='${BUILD_LD}'"

do_configure () {
        :
}

do_compile() {
        make -C src/ bin/rtl8139.rom
}

do_install() {
        install -d ${D}/usr/share/firmware
        install -m 0644 src/bin/rtl8139.rom ${D}/usr/share/firmware/
}

