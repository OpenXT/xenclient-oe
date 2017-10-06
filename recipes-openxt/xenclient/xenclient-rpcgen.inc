DESCRIPTION = "rpc stubs generation mechanism"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    dbus \
    hkg-dbus-core \
    libxslt-native \
"

PV = "0+git${SRCPV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/idl.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git"

TEMPLATES_DIR="xc-rpcgen-1.0/templates"

inherit haskell 
