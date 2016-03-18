require recipes-devtools/ghc/ghc-pkg.inc

DESCRIPTION = "XenClient DB upgrade utility"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "ghc-native ghc-text ghc-mtl ghc-network ghc-json"
RDEPENDS_${PN} += "glibc-gconv-utf-32 ghc-runtime"

# Ocaml stuff is built with the native compiler with "-m32".

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/manager.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/upgrade-db"

inherit xenclient

do_install() {
	install -m 0755 -d ${D}/usr
	install -m 0755 -d ${D}/usr/bin
	runhaskell Setup.hs copy --destdir=${D}
}
