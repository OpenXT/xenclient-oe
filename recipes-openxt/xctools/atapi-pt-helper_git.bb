DESCRIPTION = "Stubdomain atapi-cdrom helper)"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libargo xen-tools"

require xctools.inc

FILES:${PN} += "/usr/lib/xen/bin/atapi_pt_helper"
FILES:${PN}-dbg += " /usr/lib/xen/bin/.debug "

S = "${UNPACKDIR}/${BP}/atapi_pt_helper"

ASNEEDED = ""

inherit autotools
inherit pkgconfig

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${B}/src/atapi_pt_helper ${D}/usr/lib/xen/bin/
}
