DESCRIPTION = "XenClient V4V kernel headers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"


inherit xenclient-repo

SRC_URI = "${OPENXT_GIT_MIRROR}/v4v.git;protocol=git;tag=${OPENXT_TAG}"

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



