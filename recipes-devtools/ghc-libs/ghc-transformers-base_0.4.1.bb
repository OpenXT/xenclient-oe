require recipes-devtools/ghc-libs/ghc-lib-common.inc
SRC_URI[md5sum] = "bd99282e2daae5eecd1c953b7b77c990"
SRC_URI[sha256sum] = "4fa9e8ae38f0ed0633251f7a18c51946ee9fa486ed768389b5b6edaacf3c7cb4"

DEPENDS += "ghc-transformers"
# base (≥3 & <5), transformers (≥0.2)
DESCRIPTION = "Lift computations from the bottom of a transformer stack"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM="file://LICENSE;md5=93e7d84c230d8f835ebffaa82a621e39"
GHC_PN = "transformers-base"
