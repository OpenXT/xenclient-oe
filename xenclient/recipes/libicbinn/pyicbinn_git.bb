
DESCRIPTION = "Python bindings for icbinn"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

S = "${WORKDIR}/git/pyicbinn"

SRC_URI = "${OPENXT_GIT_MIRROR}/icbinn.git;protocol=git;tag=${OPENXT_TAG}"

DEPENDS = "swig-native libicbinn-resolved xenclient-rpcgen-native"
RDEPENDS += "python-lang"

inherit distutils
inherit xenclient
