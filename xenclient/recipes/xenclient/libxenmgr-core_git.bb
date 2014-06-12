require recipes/ghc/ghc-xclib.inc

DESCRIPTION = "xenmgr core"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libxch-rpc libxchdb ghc-parsec ghc-errors"
RDEPENDS += "glibc-gconv-utf-32 ghc-runtime-native"

SRC_URI = "${OPENXT_GIT_MIRROR}/manager.git;protocol=git;tag=${OPENXT_TAG}"
S = "${WORKDIR}/git/xenmgr-core"
