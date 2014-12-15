BBCLASSEXTEND = "native"
require recipes-devtools/ghc-libs/ghc-lib-common.inc
SRC_URI[md5sum] = "8bc1f6326a816d89f17f1b66e6e7b13a"
SRC_URI[sha256sum] = "4f6576eb8d26539017a8fa1dead5d5ff02f7dbbe0ce60efcb2c38cc948e84a73"
SRC_URI += "file://semigroupoids-flexible-contexts.patch;patch=1"

DEPENDS += "ghc-comonad ghc-contravariant ghc-semigroups ghc-transformers"
DESCRIPTION = "Provides a wide array of semigroupoids and operations for working with semigroupds."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=a54c8d25fd04603d961c2a2c3b866355"
GHC_PN = "semigroupoids"
