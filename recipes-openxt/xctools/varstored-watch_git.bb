SUMMARY = "Provides binary that restarts varstored on failure"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

require xctools.inc

DEPENDS = " \
    xen-tools \
"

RDEPENDS:${PN} = " \
    varstored \
"

S = "${UNPACKDIR}/${BP}/varstored-watch"

do_install() {
    oe_runmake install DESTDIR=${D}
}
