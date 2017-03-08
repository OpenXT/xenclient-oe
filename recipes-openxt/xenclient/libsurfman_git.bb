DESCRIPTION = "Library Surface Manager Plugin"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen libevent"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/surfman.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/libsurfman"

EXTRA_OEMAKE += "LIBDIR=${STAGING_LIBDIR}"

ASNEEDED = ""

inherit autotools-brokensep pkgconfig lib_package xenclient
