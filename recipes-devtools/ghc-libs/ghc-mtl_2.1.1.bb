SRC_URI[md5sum] = "0654be687f1492a2ff30cf6f3fb7eed0"
SRC_URI[sha256sum] = "9250831796b1678380d915d2953ce94fa466af8d5c92d0c569963f0f0b037a90"
BBCLASSEXTEND = "native"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-transformers"
DESCRIPTION = "Monad Transformers for ghc"
LICENSE = "GHCL"
LIC_FILES_CHKSUM = "file://LICENSE;md5=315290737f6293f698ed37113aa1226d"
GHC_PN = "mtl"
