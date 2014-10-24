require recipes/ghc/ghc-xcprog.inc

DESCRIPTION = "bash completion for human beings"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=acbef28775875450fdedef37d178f5c4"
DEPENDS = "ghc-native ghc-parsec"
RDEPENDS += "glibc-gconv-utf-32 ghc-runtime-native"

# Ocaml stuff is built with the native compiler with "-m32".

PV = "0+git${SRCPV}"

SRCREV = "80d1955ecbfe803997b3b98f5363bc76dc510478"
SRC_URI = "git://github.com/openxt/xctools.git;protocol=https"

S = "${WORKDIR}/git/compleat"

inherit xenclient

do_install() {
	install -m 0755 -d ${D}/usr
	install -m 0755 -d ${D}/usr/bin
	runhaskell Setup.hs copy --destdir=${D}

	install -m 0755 -d ${D}/etc
	install -m 0755 -d ${D}/etc/compleat.d
	install -m 0755 -d ${D}/etc/profile.d
	install -m 0755 ${S}/compleat_setup ${D}/etc/profile.d/compleat_setup.sh
}

