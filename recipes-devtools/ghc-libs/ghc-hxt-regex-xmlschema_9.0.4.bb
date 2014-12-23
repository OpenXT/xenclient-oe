SRC_URI[md5sum] = "1ff0c995ebaedaaf1289e6e65e60857c"
SRC_URI[sha256sum] = "eef1c9ae42b3d2ed78019bec6bed92034b1395ea5aa725a459df9ca5d34d884a"

require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-parsec ghc-hxt-charproperties"
DESCRIPTION = "part of hxt"
LICENSE = "MIT"
LIC_FILES_CHKSUM="file://LICENSE;md5=cb61534369f1bbb7cbe62e014d3d5717"
GHC_PN = "hxt-regex-xmlschema"
