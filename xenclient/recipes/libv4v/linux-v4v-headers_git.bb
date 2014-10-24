DESCRIPTION = "XenClient V4V kernel headers"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0+git${SRCPV}"

SRCREV = "03df72706f45e568b0862672bb4768dd6c4c15b9"
SRC_URI = "git://github.com/openxt/v4v.git;protocol=https"

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



