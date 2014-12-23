SRC_URI[md5sum] = "1e21e439c24181136ac7a77ed4c64267"
SRC_URI[sha256sum] = "d9e8d0b82d64775a1529d3747adfe820852a743e386ce75b080b040cf5959045"

require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-binary ghc-deepseq ghc-hunit ghc-hxt-charproperties ghc-hxt-regex-xmlschema ghc-hxt-unicode ghc-mtl ghc-network ghc-parsec"
DESCRIPTION = "XML library for ghc"
LICENSE = "MIT"
LIC_FILES_CHKSUM="file://LICENSE;md5=cb61534369f1bbb7cbe62e014d3d5717"
GHC_PN = "hxt"
