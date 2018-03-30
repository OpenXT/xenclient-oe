FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

DEPENDS += " \
    xz-native \
"

SRC_URI += " \
    file://early-debug.patch \
    file://xen-debug-output-driver.patch \
"
# Upstream (meta-virt) uses 8c43891db4eb131d019360ccfb619f235b17eb58.
# While Xen stable-4.9 uses 827dd1bfee67daa683935ce65316f7e0f057fe1c
# Use the later. Upgrading should be painless, and we might as well use Xen
# upstream default version to match test coverage.
SRCREV = "827dd1bfee67daa683935ce65316f7e0f057fe1c"

LIC_FILES_CHKSUM = "file://../COPYING;md5=92be9bced83819c46c5ab272173c4aa7"

EXTRA_OEMAKE_append = " HOST_CFLAGS='${BUILD_CFLAGS} ${BUILD_LDFLAGS}'"

do_compile_append() {
    oe_runmake bin/intel.rom
}
