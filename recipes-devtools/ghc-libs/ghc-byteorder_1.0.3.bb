BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "70f8e69c2fdf384bf9a44ee3478f6a1c"
SRC_URI[sha256sum] = "5ef1125f2c6506ba6303f59f3265b4d0caaa6dbe897cf14dac715e920f59d214"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DESCRIPTION = "Exposes the native endianness or byte ordering of the system."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7bfb975591c3d1d9370d2450eb251342"
GHC_PN = "byteorder"
