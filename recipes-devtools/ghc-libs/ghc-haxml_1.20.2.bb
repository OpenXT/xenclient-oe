BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "9635c348e70c0446e74783e7c267050c"
SRC_URI[sha256sum] = "c32c10b95446ecb938dc6cd34585187efd3fcb4b21f7d0c7cbd646ba94c87516"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-polyparse"
DESCRIPTION = "XML library for ghc"
# Clause 6 relaxed.
LICENSE = "LGPLv3 & GPLv3"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=b84b8bea272e7357c5c7fe6f255ba732"
GHC_PN = "HaXml"

INSANE_SKIP_${PN}-utils = "already-stripped"

FILES_${PN}-utils += "${bindir}/*"

