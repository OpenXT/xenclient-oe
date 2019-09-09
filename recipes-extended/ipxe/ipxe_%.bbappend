FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

DEPENDS += " \
    xz-native \
"

SRC_URI += " \
    file://early-debug.patch \
    file://xen-debug-output-driver.patch \
"

LIC_FILES_CHKSUM = "file://../COPYING;md5=92be9bced83819c46c5ab272173c4aa7"

EXTRA_OEMAKE_append = " HOST_CFLAGS='${BUILD_CFLAGS} ${BUILD_LDFLAGS}'"

do_compile_append() {
    oe_runmake bin/intel.rom
    oe_runmake bin/82540em.rom
}
