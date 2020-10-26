DESCRIPTION = "Stubdomain QMP helper"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "libargo"

require xctools.inc

FILES_${PN} += "/usr/lib/xen/bin/qmp_helper"
FILES_${PN}-dbg += " /usr/lib/xen/bin/.debug "

S = "${WORKDIR}/git/qmp_helper"

ASNEEDED = ""

inherit autotools
inherit pkgconfig

do_install(){
        install -d ${D}/usr/lib/xen/bin
        install -m 755 ${B}/src/qmp_helper ${D}/usr/lib/xen/bin/
}
