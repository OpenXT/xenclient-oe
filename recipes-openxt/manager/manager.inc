PV = "0+git${SRCPV}"
SRC_URI = "git://github.com/OpenXT/manager.git;protocol=https"
SRCREV = "${AUTOREV}"

# needed for ghc-provided libffi
RDEPENDS_${PN} += "\
    ghc-runtime \
"
