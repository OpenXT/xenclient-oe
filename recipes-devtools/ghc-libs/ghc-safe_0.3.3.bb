BBCLASSEXTEND = "native"
require recipes-devtools/ghc-libs/ghc-lib-common.inc
SRC_URI[md5sum] = "5df09a74ef9ce6d98c7fd7f97a9eb73e"
SRC_URI[sha256sum] = "099154c843ac9c4b1831b06f0a92f56d6bba2b0163c9aa896031921bb0a2e945"

DEPENDS += ""
DESCRIPTION = "Partial functions from the base library, such as head and !!, modified to return more descriptive error messages, programmer defined error messages, Maybe wrapped results and default values. These functions can be used to reduce the number of unsafe pattern matches in your code."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=f5138d63fd9e7ecca79f04796e43bda4"
GHC_PN = "safe"
