DESCRIPTION = "Companion app to externalise host pci config space requests within qemu"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libv4v pciutils libxcxenstore "

SRC_URI = "${OPENXT_GIT_MIRROR}/xctools.git;protocol=git;tag=${OPENXT_TAG}"

FILES_${PN} += "/usr/lib/xen/bin/pci-dm-helper"

S = "${WORKDIR}/git/pci-dm-helper"

inherit autotools
inherit pkgconfig
inherit xenclient

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${S}/src/pci-dm-helper ${D}/usr/lib/xen/bin/
}
