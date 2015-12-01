require recipes-devtools/ghc/ghc-pkg.inc

BBCLASSEXTEND = "native"

DESCRIPTION = "Haskell VHD tool"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENCE;md5=cc8224b3041a54c20bd7becce249bb02"
DEPENDS = "ghc-native ghc-vhd"
RDEPENDS_${PN} += "glibc-gconv-utf-32 ghc-runtime"

SRC_URI = "git://github.com/jonathanknowles/hs-vhd;protocol=git;tag=0.2"

S = "${WORKDIR}/git"

CABAL_CONFIGURE_EXTRA_OPTS = "--flag=executable"


do_install() {
	install -d "${D}/${bindir}"
	install -m 755  "${S}/dist/build/vhd/vhd" "${D}/${bindir}"
}
