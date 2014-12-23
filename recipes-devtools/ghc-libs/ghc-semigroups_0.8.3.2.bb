BBCLASSEXTEND = "native"
require recipes-devtools/ghc-libs/ghc-lib-common.inc
SRC_URI += "file://semigroups-cabal-cheat.patch;patch=1 file://semigroups-derive-data-typeable.patch;patch=2"
SRC_URI[md5sum] = "f406607e0421554cde40594b65f83086"
SRC_URI[sha256sum] = "ecd8b368215fbd697f6508dcac77b153ac02b9a1568694bdc886d998091d833c"

DEPENDS += ""
DESCRIPTION = "Haskell 98 semigroups"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=a54c8d25fd04603d961c2a2c3b866355"
GHC_PN = "semigroups"
