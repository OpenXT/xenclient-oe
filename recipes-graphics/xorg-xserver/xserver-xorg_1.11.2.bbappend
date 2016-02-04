PR .= ".2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "\
    file://compile-with-GCC5.patch \
    "

CFLAGS_append += "-Wno-error=redundant-decls"