BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "6bf8f3d1441602c9ab09a75e3bd6e926"
SRC_URI[sha256sum] = "e0c2dede617dc9b1611f62f2801f21d2fd48e044ee6886e77b55df10e0f2130b"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += ""
DESCRIPTION = "Binary serialisation for GHC"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=842154b7fc56299acd68b1fe3fcd79b2"
GHC_PN = "binary"
SETUPFILE = "Setup.lhs"
