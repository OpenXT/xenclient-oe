FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://0002-efi-Fix-sanity-check-in-dbg_efi_protocols.patch;striplevel=2 \
    file://early-debug.patch \
    file://xen-debug-output-driver.patch \
"

LIC_FILES_CHKSUM = "file://../COPYING;md5=92be9bced83819c46c5ab272173c4aa7"

EXTRA_OEMAKE_append = " HOST_CFLAGS='${BUILD_CFLAGS} ${BUILD_LDFLAGS}'"

do_compile_append() {
    oe_runmake bin/intel.rom
    oe_runmake bin/82540em.rom
}
