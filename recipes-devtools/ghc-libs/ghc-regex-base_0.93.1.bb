BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "194b940de71fc3f50780cff8569ba046"
SRC_URI[sha256sum] = "24a0e76ab308517a53d2525e18744d9058835626ed4005599ecd8dd4e07f3bef"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-mtl"
DESCRIPTION = "Regex library for GHC"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=5a9760d05990120048023e30741b4d71"
GHC_PN = "regex-base"
