BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "310bf233dcf8ec678c427b1198700b53"
SRC_URI[sha256sum] = "962d39944bae18b0fea60961c77513f455f95c0f67ae4b10ab15484a27b6fb98"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-mtl"
DESCRIPTION = "Monadic parser combinators for GHC"
# But not canonical...
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4b36c87a94916f61cc02d0d4211317ff"
GHC_PN = "parsec"
