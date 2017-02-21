SRC_URI[md5sum] = "305f61ffd98c4c03eb0d9b7749897e59"
SRC_URI[sha256sum] = "6044f24a44053684d1260f19387e59359f59b0605cdbf7295e1de42783e48ff1"
DESCRIPTION = "caml4p"
DEPENDS = "ocaml-cross ocaml-ocamlbuild"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=77f085d1023152a31ada8288ffd6e8f1"

inherit cross

PR = "r0"

SRC_URI = "https://github.com/ocaml/camlp4/archive/4.04+1.tar.gz \
"

S = "${WORKDIR}/camlp4-${PV}"

RDEPENDS_${PN}-dev = ""

do_configure() {
	./configure --bindir=${bindir} --libdir=${libdir}/ocaml
}

do_compile() {
	# make all tries to build native bits...
	oe_runmake
}

do_install() {
	oe_runmake install
}
