DESCRIPTION = "Python bindings for icbinn"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

require icbinn.inc

S = "${WORKDIR}/git/pyicbinn"

DEPENDS = "swig-native libicbinn-resolved xenclient-rpcgen-native"

inherit distutils3
