SRC_URI[md5sum] = "108717618e724295d8a01c21ba3f7311"
SRC_URI[sha256sum] = "5d4b9a79e9abf8be0b509f6b8cf5696221cbe14fa2fbb2bb352342755fd15eef"
DESCRIPTION = "findlib"
DEPENDS = "ocaml-cross ocaml-ocamlbuild ocaml-camlp4"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a30ace4f9508a47d2c25c45c48af6492"

inherit cross

PR = "r0"

SRC_URI = "http://download.camlcity.org/download/findlib-1.7.1.tar.gz \
           file://findlib_remote_utime.patch;patch=1 \
"

S = "${WORKDIR}/findlib-${PV}"

RDEPENDS_${PN}-dev = ""

do_configure() {
	./configure -bindir ${bindir} -mandir ${datadir}/man -config /non/existant/config.conf 
}

do_compile() {
	oe_runmake all
	oe_runmake opt
}

do_install() {
	oe_runmake prefix=${D} install
}
