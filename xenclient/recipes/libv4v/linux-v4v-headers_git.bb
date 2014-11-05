DESCRIPTION = "XenClient V4V kernel headers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/v4v.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git/v4v"

do_configure() {
:
}

do_compile() {
:
}

do_install(){
    install -d ${D}${includedir}/linux
    install ${S}/linux/v4v_dev.h ${D}${includedir}/linux/
}



