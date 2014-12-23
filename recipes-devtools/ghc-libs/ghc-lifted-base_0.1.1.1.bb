require recipes-devtools/ghc-libs/ghc-lib-common.inc
SRC_URI += "file://lifted-base-cabal-cheat.patch;patch=1"

SRC_URI[md5sum] = "f642df016e04eea4ad494bbaabed2bdd"
SRC_URI[sha256sum] = "e0445a9a67341236b2b852bb627cca67d5b79a770f04c2b7cbd9432c821dd3b1"

DEPENDS += "ghc-base-unicode-symbols ghc-monad-control ghc-transformers-base"
# base-unicode-symbols (≥0.1.1 & <0.3), monad-control (0.3.*), transformers-base
DESCRIPTION = "Lifted IO operations from the base library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=67634a94867d2b4cea9192052bca1335"
GHC_PN = "lifted-base"

