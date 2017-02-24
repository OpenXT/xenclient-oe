SRC_URI[md5sum] = "000d2ebad1333f9afcccdcd68c19f14d"
SRC_URI[sha256sum] = "2603be3709634b6191dd00627213cff56f15200f2d0a24e0af58a18a0580b71e"
DESCRIPTION = "ocamlbuild"
DEPENDS = "ocaml-cross"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5123b1988300c0d24c79e04f09d86dc0"

inherit cross

PR = "r0"

SRC_URI = "https://github.com/ocaml/ocamlbuild/archive/0.10.1.tar.gz \
"

S = "${WORKDIR}/ocamlbuild-${PV}"

RDEPENDS_${PN}-dev = ""

do_configure() {
	oe_runmake configure OCAMLBUILD_PREFIX=${prefix} OCAMLBUILD_BINDIR=${bindir} \
		OCAMLBUILD_LIBDIR=${libdir}/ocaml OCAMLBUILD_MANDIR=${datadir}/man \
		OCAML_NATIVE=false OCAML_NATIVE_TOOLS=false
}

do_compile() {
	oe_runmake
}

do_install() {
	oe_runmake install
}
