inherit module-compat
inherit xenclient

DESCRIPTION = "Superhid kernel module"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"

PV = "0+git${SRCPV}"

SRCREV = "6b439ff65933f312e3554748b76d50b402ea54e9"
SRC_URI = "git://github.com/openxt/input.git;protocol=https"

S = "${WORKDIR}/git/superhid"

MAKE_TARGETS += "modules"
