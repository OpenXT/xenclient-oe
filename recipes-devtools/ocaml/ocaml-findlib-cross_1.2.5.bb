SRC_URI[md5sum] = "db12a92903c2869bb89e1a6446db269e"
SRC_URI[sha256sum] = "9e1cf8ca3aa82329e1004612dae3060b2e24d4610c439d23575f72c88ddea868"
DESCRIPTION = "findlib"
DEPENDS = "ocaml-cross"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a30ace4f9508a47d2c25c45c48af6492"

inherit cross

PR = "r0"

SRC_URI = "http://download.camlcity.org/download/findlib-${PV}.tar.gz \
           file://findlib_remote_utime.patch;patch=1"

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

#do_stage() {
#	oe_runmake prefix=/ install
#}
