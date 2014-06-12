DESCRIPTION = "Xenstore library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://../COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen-tools libxclogging libevent"

SRC_URI = "${OPENXT_GIT_MIRROR}/xclibs.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git/xcxenstore"

inherit autotools
inherit xenclient
