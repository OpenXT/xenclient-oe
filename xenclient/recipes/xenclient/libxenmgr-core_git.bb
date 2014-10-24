require recipes/ghc/ghc-xclib.inc

DESCRIPTION = "xenmgr core"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libxch-rpc libxchdb ghc-parsec ghc-errors"
RDEPENDS += "glibc-gconv-utf-32 ghc-runtime-native"

PV = "0+git${SRCPV}"

SRCREV = "71bfc70ec028c744e84914dc7ffcdccdd499c8c8"
SRC_URI = "git://github.com/openxt/manager.git;protocol=https"
S = "${WORKDIR}/git/xenmgr-core"
