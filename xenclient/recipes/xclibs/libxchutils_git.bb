require recipes/ghc/ghc-xclib.inc

DESCRIPTION = "haskell misc utilities"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "ghc-mtl ghc-text ghc-json ghc-hsyslog"
RDEPENDS += "glibc-gconv-utf-32 ghc-runtime-native"

PV = "0+git${SRCPV}"

SRCREV = "623de9891719926c54d71456e34ab71feb2694cf"
SRC_URI = "git://github.com/openxt/xclibs.git;protocol=https"
S = "${WORKDIR}/git/xchutils"
