DESCRIPTION = "helps accessing db daemon"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "libxch-rpc xenclient-rpcgen-native xenclient-idl hkg-mtl hkg-text hkg-json libxchutils"
RDEPENDS_${PN} += "glibc-gconv-utf-32"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/xclibs.git"

S = "${WORKDIR}/git/xchdb"

HPN = "xchdb"
HPV = "0.1"

require xclibs.inc
inherit haskell xc-rpcgen

do_configure_append() {
    mkdir -p Rpc/Autogen
    xc-rpcgen --haskell --templates-dir=${STAGING_RPCGENDATADIR_NATIVE} -c -o Rpc/Autogen --module-prefix=Rpc.Autogen ${STAGING_IDLDATADIR}/db.xml
}
