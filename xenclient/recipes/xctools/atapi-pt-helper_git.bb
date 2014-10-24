DESCRIPTION = "Stubdomain atapi-cdrom helper)"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libv4v"

PV = "0+git${SRCPV}"

SRCREV = "80d1955ecbfe803997b3b98f5363bc76dc510478"
SRC_URI = "git://github.com/openxt/xctools.git;protocol=https"

FILES_${PN} += "/usr/lib/xen/bin/atapi_pt_helper"

S = "${WORKDIR}/git/atapi_pt_helper"

inherit autotools
inherit pkgconfig
inherit xenclient

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${S}/src/atapi_pt_helper ${D}/usr/lib/xen/bin/
}
