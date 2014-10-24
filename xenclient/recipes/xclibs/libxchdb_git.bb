require recipes/ghc/ghc-xclib.inc

DESCRIPTION = "helps accessing db daemon"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "libxch-rpc xenclient-rpcgen-native xenclient-idl ghc-mtl ghc-text ghc-json libxchutils"
RDEPENDS += "glibc-gconv-utf-32 ghc-runtime-native"

PV = "0+git${SRCPV}"

SRCREV = "623de9891719926c54d71456e34ab71feb2694cf"
SRC_URI = "git://github.com/openxt/xclibs.git;protocol=https"
S = "${WORKDIR}/git/xchdb"

do_configure_append() {
    mkdir -p Rpc/Autogen
    xc-rpcgen --haskell -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_DATADIR}/idl/db.xml
}
