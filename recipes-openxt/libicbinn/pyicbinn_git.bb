
DESCRIPTION = "Python bindings for icbinn"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

S = "${WORKDIR}/git/pyicbinn"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/OpenXT/icbinn.git"

DEPENDS = "swig-native libicbinn-resolved xenclient-rpcgen-native"
RDEPENDS_${PN} += "python-lang"

inherit distutils
