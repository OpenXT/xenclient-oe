BBCLASSEXTEND = "native"
require recipes-devtools/ghc-libs/ghc-lib-common.inc
SRC_URI[md5sum] = "8e2b27994ae503609b42bb19316bfa1b"
SRC_URI[sha256sum] = "2d693c0df0393faaa27040c9c9b7246c8efece3bb4e814de8854eac79af491d1"

SRC_URI += "file://errors-cheat-cabal.patch;patch=1"

DEPENDS += "ghc-either ghc-safe ghc-transformers"
DESCRIPTION = "The one-stop shop for all your error-handling needs! Just import Control.Error."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=de760811a0a688464f4e84b2d717c9b9"
GHC_PN = "errors"
