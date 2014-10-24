
DESCRIPTION = "Python bindings for icbinn"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

S = "${WORKDIR}/git/pyicbinn"

PV = "0+git${SRCPV}"

SRCREV = "4b4b14b9d3929413bab3a48992f4cfb03f1355d7"
SRC_URI = "git://github.com/openxt/icbinn.git;protocol=https"

DEPENDS = "swig-native libicbinn-resolved xenclient-rpcgen-native"
RDEPENDS += "python-lang"

inherit distutils
inherit xenclient
