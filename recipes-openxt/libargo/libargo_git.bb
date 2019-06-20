DESCRIPTION = "XenClient Argo library and interposer"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen argo-module-headers"

PV = "git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/linux-xen-argo.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
	   file://13-argo.rules"

S = "${WORKDIR}/git/libargo"

inherit autotools-brokensep pkgconfig lib_package xenclient

EXTRA_OECONF += "--with-pic"

do_install_append(){
    install -d ${D}/etc
    install -d ${D}/etc/udev
    install -d ${D}/etc/udev/rules.d
    install ${WORKDIR}/13-argo.rules ${D}/etc/udev/rules.d
}

