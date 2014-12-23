BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "7faf43a94a0082ee2fe7971fabd9be21"
SRC_URI[sha256sum] = "a2f841430fec32edba778b74bde83bf0170ada7c5e2e59d7187c8f06d92dcca9"

require recipes-devtools/ghc-libs/ghc-lib-common.inc

DEPENDS += ""
# base (≥3.0 & <3.0.3.1) or base (≥3.0.3.1 & <4.6)
DESCRIPTION = "Unicode alternatives for common functions and operators"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=91b1bcbe3d4f36632f0400276e50e1dc"
GHC_PN = "base-unicode-symbols"
