BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "4d727ca01e55884d642613593edb47f5"
SRC_URI[sha256sum] = "f15dd0127cbaaaa3fef69bfde6e2ac8b83e9d3a0295bf94de8c0c9e8c928e375"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-mmap ghc-storable-endian ghc-cereal ghc-text"
DESCRIPTION = "Implementation of the Virtual Hard Disk (VHD) disk format"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENCE;md5=cc8224b3041a54c20bd7becce249bb02"
GHC_PN = "vhd"
