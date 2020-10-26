DESCRIPTION = "XenClient Argo kernel headers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

require argo.inc

S = "${WORKDIR}/git/argo-linux"

do_configure() {
:
}

do_compile() {
:
}

do_install(){
    install -d ${D}${includedir}/linux
    install ${S}/include/linux/argo_dev.h ${D}${includedir}/linux/
}
