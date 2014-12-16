DESCRIPTION = "Utility to create a pre-allocated file"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://falloc.c;beginline=6;endline=21;md5=bceb440312cb07ee6e92b297dd087767"

PR = "r0"

SRC_URI = "file://falloc.c"
S = "${WORKDIR}"

do_compile() {
	oe_runmake falloc
}

do_install() {
	install -d ${D}/usr/bin
	install -m 0755 falloc ${D}/usr/bin
}
