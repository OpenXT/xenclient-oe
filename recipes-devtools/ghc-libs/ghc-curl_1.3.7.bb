SRC_URI[md5sum] = "5fc326cec592b0e2085b7884e079428b"
SRC_URI[sha256sum] = "f1410ca81fffff241678d287588b201285c6c9637d6db5d90af6952bc639cd44"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "curl"
DESCRIPTION = "Haskell binding to libcurl. libcurl is a client-side URL transfer library."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=d79e207d823135a69350c825da30fc86"
GHC_PN = "curl"
