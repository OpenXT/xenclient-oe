BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "41194f8633be8e30cacad88146dbf7c2"
SRC_URI[sha256sum] = "947c45e7ee862159f190fb8e905c1328f7672cb9e6bf3abd1d207bbcf1eee50a"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += ""
DESCRIPTION = "Fully evaluate data structures for GHC"
LICENSE = "GHCL"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a83ef7d4aeff27103dc2d40e9a70c2e6"

GHC_PN = "deepseq"
