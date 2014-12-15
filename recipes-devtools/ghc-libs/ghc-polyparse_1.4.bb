BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "1783867124b49eefdbdc9ac39caf3174"
SRC_URI[sha256sum] = "6e599fb0771e8ce2e1d3a3bbe5eddc2d77b2b4bbb54602f01005dc55dc039d44"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += ""
DESCRIPTION = "A variety of alternative parser combinator libraries for GHC"
# Version not clearly specified.
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=b0c0e150083a5c140eda79fd10acf348"
GHC_PN = "polyparse"
