BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "019036c9111b521654eab2b9dee7648d"
SRC_URI[sha256sum] = "a6daa0ee43ddede620363ab26614fef69361bd5b8f77aa6918b5a4ecb083f425"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += "ghc-deepseq"
DESCRIPTION = "An efficient packed Unicode text type"
# Not canonical.
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3137a2b6a6df0a4fab97e00be8dff1a2"
GHC_PN = "text"
SETUPFILE = "Setup.lhs"
