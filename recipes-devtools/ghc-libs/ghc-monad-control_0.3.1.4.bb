BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "ea20f9fa1d65db2ae60afeb8954b547e"
SRC_URI[sha256sum] = "c17fd5fa094044816ab79158a00fa7a9fd8b35dfea27e1eecc4d4049b3916c57"

require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-base-unicode-symbols ghc-transformers ghc-transformers-base"
# base (≥3 & <4.6), base-unicode-symbols (≥0.1.1 & <0.3), transformers (≥0.2 & <0.4), transformers-base (≥0.4.1 & <0.5)
DESCRIPTION = "Lift control operations, like exception catching, through monad transformers"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=67634a94867d2b4cea9192052bca1335"
GHC_PN = "monad-control"
