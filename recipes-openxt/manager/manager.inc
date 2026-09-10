PV = "0+git${SRCPV}"
SRC_URI = "git://github.com/OpenXT/manager.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

# needed for ghc-provided libffi
RDEPENDS:${PN} += "\
    ghc-runtime \
"
