DESCRIPTION = "helps accessing db daemon"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "libxch-rpc xenclient-rpcgen-native xenclient-idl hkg-mtl hkg-text hkg-json libxchutils rpc-autogen"
RDEPENDS_${PN} += "glibc-gconv-utf-32"

require xclibs.inc

S = "${WORKDIR}/git/xchdb"

HPN = "xchdb"
HPV = "0.1"

require xclibs-haskell.inc
