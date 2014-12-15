BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "f308cb97953e65637bf75d0c39a67266"
SRC_URI[sha256sum] = "610f2888ca11362e012e1ff9ff7269862fad505bc24ba80ff1fa66b9a6e5681b"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DESCRIPTION = "Some useful control operators for looping."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=646bf488d536d67d7116b3de18ab85f2"
GHC_PN = "monad-loops"
SETUPFILE = "Setup.lhs"
