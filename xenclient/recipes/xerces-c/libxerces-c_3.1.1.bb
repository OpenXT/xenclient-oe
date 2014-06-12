SUMMARY = "Xerces-C++ XML parser"
DESCRIPTION = "Xerces-C++ XML parser"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
DEPENDS = "curl"
PACKAGES += "${PN}-utils"

PR="r1"

SRC_URI = "http://www.apache.org/dist/xerces/c/3/sources/xerces-c-${PV}.tar.gz"
SRC_URI[md5sum] = "6a8ec45d83c8cfb1584c5a5345cb51ae"
SRC_URI[sha256sum] = "a42785f71e0b91d5fd273831c87410ce60a73ccfdd207de1b805d26d44968736"
S = "${WORKDIR}/xerces-c-${PV}"

# Library name is not standard, split link from binary.
FILES_${PN} = "${libdir}/${PN}-*.so"

# rpath is screwed (lib/..//lib).
INSANE_SKIP_${PN} = "rpaths"
INSANE_SKIP_${PN}-dev = "rpaths"
INSANE_SKIP_${PN}-utils = "rpaths"

FILES_${PN}-utils = "${bindir}/*"

inherit autotools
inherit pkgconfig

do_configure() {
    oe_runconf --enable-transcoder-iconv --enable-msgloader-inmemory --without-icu
}
