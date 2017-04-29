DESCRIPTION = "v4v kernel module headers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://v4v.h;beginline=6;endline=32;md5=8054a75b345d2cd08e16f9dd0ad9283b"

PV = "git${SRCPV}"

DEPENDS = ""

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/v4v.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/v4v"

INHIBIT_DEFAULT_DEPS = "1"
EXCLUDE_FROM_SHLIBS = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

do_configure() {
}

do_compile() {
}

do_install() {
    install -m 0755 -d ${D}/usr/include/linux
    install -m 0755 -d ${STAGING_KERNEL_DIR}/include/xen
    install -m 0755 -d ${STAGING_KERNEL_DIR}/include/linux
    install -m 644 include/xen/v4v.h ${STAGING_KERNEL_DIR}/include/xen/v4v.h
    install -m 644 linux/v4v_dev.h ${D}/usr/include/linux/v4v_dev.h
    install -m 644 linux/v4v_dev.h ${STAGING_KERNEL_DIR}/include/linux/v4v_dev.h
}

STAGING_KERNEL_DIR[vardepsexclude] = "MACHINE"
do_install[vardepsexclude] = "MACHINE"
