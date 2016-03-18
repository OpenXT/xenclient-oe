DESCRIPTION = "Companion app to externalise host pci config space requests within qemu"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libv4v pciutils libxcxenstore "

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xctools.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

FILES_${PN} += "/usr/lib/xen/bin/pci-dm-helper"

S = "${WORKDIR}/git/pci-dm-helper"

ASNEEDED = ""

inherit autotools
inherit pkgconfig
inherit xenclient

CFLAGS_append += " -Wno-error=cpp "

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${B}/src/pci-dm-helper ${D}/usr/lib/xen/bin/
}
