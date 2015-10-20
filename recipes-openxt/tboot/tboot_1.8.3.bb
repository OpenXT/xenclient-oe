
require recipes-openxt/tboot/tboot.inc

# This is not a very good way to do this. Some update to this file like a
# change to the copyright date broke the checksum.
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://tboot/include/types.h;beginline=4;endline=32;md5=5a2b57a442b97cfc3d81ba639fda3ac1"

PR = "r1"

S="${WORKDIR}/tboot-1.8.3"

SRC_URI = "http://downloads.sourceforge.net/tboot/tboot-1.8.3.tar.gz \
           file://tboot-build-fixes.patch \
           file://tboot-adjust-grub2-modules.patch \
           file://tboot-fix-skip-cmdline-arg-CVE-2014-5118.patch \
           file://tboot-warn-on-failure-policy.patch \
           file://tboot-lz-logging-bugs-workaround.patch \
           file://tboot-txt-stat-fix-backport-cs426.patch \
           "
SRC_URI[md5sum] = "6c61f4db468cf50baf7adbeaac1231e7"
SRC_URI[sha256sum] = "2f2e0c3865b45691f76b31730c5aaea2f076e7949ee6309e78ed7f80d8c53d39"

ASNEEDED = ""

do_compile_append() {
	oe_runmake -C ${S}/lcptools-v2 CFLAGS+="-std=c99"
}

do_install_append() {
	oe_runmake -C ${S}/lcptools-v2 install DISTDIR=${D}
}
