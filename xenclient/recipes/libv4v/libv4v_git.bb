DESCRIPTION = "XenClient V4V library and interposer"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen-tools linux-v4v-headers v4v-module"

SRC_URI = "${OPENXT_GIT_MIRROR}/v4v.git;protocol=git;tag=${OPENXT_TAG} \
	   file://13-v4v.rules"

S = "${WORKDIR}/git/libv4v"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient


do_install_append(){
    install -d ${D}/etc
    install -d ${D}/etc/udev
    install -d ${D}/etc/udev/rules.d
    install ${WORKDIR}/13-v4v.rules ${D}/etc/udev/rules.d
}

