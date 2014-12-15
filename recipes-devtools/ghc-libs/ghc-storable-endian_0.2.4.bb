BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "1b0c73f6153e8b5d4864adc0583bfda6"
SRC_URI[sha256sum] = "473e22c159b58009e84eb37099448ba86337fd07bbe6b2bf7a405f9b0f1a8747"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-byteorder"
DESCRIPTION = "Storable instances with endianness"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4f801f10bb1613d14b5f06070c0d1f58"
GHC_PN = "storable-endian"
