DESCRIPTION = "Xenstore library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen-tools libxclogging libevent"

PV = "0+git${SRCPV}"

PR = "r1"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xclibs.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
           "

S = "${WORKDIR}/git/xcxenstore"

ASNEEDED = ""

inherit autotools-brokensep pkgconfig
