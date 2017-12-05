DESCRIPTION = "rpc stubs generation mechanism"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    dbus \
    hkg-dbus-core \
    libxslt-native \
"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/idl.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git"

inherit haskell 

# This has to stay consistent with xc-rpcgen.bbclass.
TEMPLATES_DIR="xc-rpcgen-${RPCGEN_VERSION}/templates"
