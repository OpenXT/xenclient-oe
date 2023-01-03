DESCRIPTION = "rpc stubs generation mechanism"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = " \
    dbus \
    hkg-dbus \
    hkg-haxml \
    libxslt-native \
"

require idl.inc

inherit haskell

# This has to stay consistent with xc-rpcgen.bbclass.
RPCGEN_VERSION = "1.0"
TEMPLATES_DIR="xc-rpcgen-${RPCGEN_VERSION}/templates"

BBCLASSEXTEND = "native"

S = "${WORKDIR}/git/rpcgen"

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${B}/dist/build/xc-rpcgen/xc-rpcgen ${D}/${bindir}
    install -m 0755 -d ${D}/${datadir}/${TEMPLATES_DIR} # share
    install -m 0644 ${S}/templates/* ${D}/${datadir}/${TEMPLATES_DIR}/
}
