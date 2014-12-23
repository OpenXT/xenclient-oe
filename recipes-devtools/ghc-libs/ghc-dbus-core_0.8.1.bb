BBCLASSEXTEND = "native"
SRC_URI[md5sum] = "f635e99147a6ef7b1fa3212c6767fa83"
SRC_URI[sha256sum] = "6da338e54d1cd4bcfbbc12bf6af08f7e90b420cb809f59e8aa94451cd17e08d1"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

SRC_URI += "file://expose-customisable-transport.patch;patch=1 \
            file://functor-fix.patch;patch=1 \
"

DEPENDS += "ghc-binary ghc-data-binary-ieee754 ghc-haxml ghc-mtl ghc-network ghc-parsec ghc-text"
DESCRIPTION = "DBus core bindings for ghc"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://License.txt;md5=d32239bcb673463ab874e80d47fae504"
GHC_PN = "dbus-core"

# This package is obsolete: use dbus instead.
