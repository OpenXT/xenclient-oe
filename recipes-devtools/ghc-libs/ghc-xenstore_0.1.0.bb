require recipes-devtools/ghc-libs/ghc-lib-common.inc
SRC_URI[md5sum] = "732fdea48c7d7b1500227838793d2be4"
SRC_URI[sha256sum] = "fb494f76b5aa415972e06ba5a76964c06c448199e8a968a332595e5879b4fc81"

DESCRIPTION = "Haskell bindings for XenStore"
DEPENDS += "ghc-cereal ghc-mtl ghc-network"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=af37f6ace7c98678b40104af99e85b6a"
GHC_PN = "xenstore"
