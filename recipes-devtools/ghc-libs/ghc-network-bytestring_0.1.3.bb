SRC_URI[md5sum] = "c02cdc374f60700e87119c4a2863394a"
SRC_URI[sha256sum] = "d2b5e9ccfef15ca8ea2d00fede066f73d1cc9e532ca69c9f7a440b8948e2b746"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-network"
DESCRIPTION = "Networking-related facilities for GHC (Bytestring)"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=c16a0a3ed33e5c94fe58ab3de6ea09f8"
GHC_PN = "network-bytestring"

# This package is obsolete: use network instead.
