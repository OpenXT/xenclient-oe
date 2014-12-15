BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "566cfeef09ff4d2e52110ec4a9a9879b"
SRC_URI[sha256sum] = "0e65b28a60764245c1ab6661a3566f286feb36e0e6f0296d6cd2b84adcd45d58"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-parsec"
DESCRIPTION = "Networking-related facilities for GHC"
LICENSE = "GHCL"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7cb08deb79c4385547f57d6bb2864e0f"
GHC_PN = "network"
