SRC_URI[md5sum] = "2168e8448aa4b0325a58cb20549670fd"
SRC_URI[sha256sum] = "c26ddd1ea4c732c2e403fee8c18e4ebad868430f2afc350c612766a9a2dfda6c"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-mtl"
DESCRIPTION = "Prompt monad for encapsulating stateful computations"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=18f444a4a5b120cd95f51003afaae2a7"
GHC_PN = "MonadPrompt"
