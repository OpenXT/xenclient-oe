DESCRIPTION = "rpc stubs generation mechanism"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "dbus ghc-native ghc-dbus-core libxslt-native"

PV = "0+git${SRCPV}"

SRC_URI = "git://github.com/openxt/idl.git;protocol=https"
S = "${WORKDIR}/git"
TEMPLATES_DIR="xc-rpcgen-1.0/templates"
EXCLUSIVE_CONFIGURE_LOCK = "1"


inherit xenclient
