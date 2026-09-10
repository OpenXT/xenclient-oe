DESCRIPTION = "Python bindings for icbinn"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

require icbinn.inc

S = "${UNPACKDIR}/${BP}/pyicbinn"

DEPENDS = "swig-native libicbinn xenclient-rpcgen-native"

inherit setuptools3
