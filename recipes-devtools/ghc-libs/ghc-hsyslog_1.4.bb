SRC_URI[md5sum] = "2a8bd79ba3a374c15151b7b03a9c65ce"
SRC_URI[sha256sum] = "f3bf4bf47565cb0245afb0e8ffa3f79635b02f0032081845a5999964d828f4db"
require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += ""
DESCRIPTION = "syslog for GHC"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=5e6c932b646ccd55858ca0f522bbd186"
GHC_PN = "hsyslog"
SETUPFILE = "Setup.lhs"
