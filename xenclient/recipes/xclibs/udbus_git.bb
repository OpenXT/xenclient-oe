require recipes/ghc/ghc-xclib.inc

DESCRIPTION = "haskell dbus library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=784a6790a51378ef1cc78d5c6999b241"
DEPENDS += "ghc-binary ghc-cereal ghc-mtl ghc-network"
RDEPENDS += "glibc-gconv-utf-32 ghc-runtime-native"

PV = "0+git${SRCPV}"

SRCREV = "623de9891719926c54d71456e34ab71feb2694cf"
SRC_URI = "git://github.com/openxt/xclibs.git;protocol=https"
S = "${WORKDIR}/git/udbus"
