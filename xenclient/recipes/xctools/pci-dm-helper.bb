DESCRIPTION = "Companion app to externalise host pci config space requests within qemu"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libv4v pciutils libxcxenstore "

PV = "0+git${SRCPV}"

SRCREV = "80d1955ecbfe803997b3b98f5363bc76dc510478"
SRC_URI = "git://github.com/openxt/xctools.git;protocol=https"

FILES_${PN} += "/usr/lib/xen/bin/pci-dm-helper"

S = "${WORKDIR}/git/pci-dm-helper"

inherit autotools
inherit pkgconfig
inherit xenclient

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${S}/src/pci-dm-helper ${D}/usr/lib/xen/bin/
}
