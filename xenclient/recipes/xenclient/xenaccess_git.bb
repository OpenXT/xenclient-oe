
inherit xenclient
inherit xenclient-pq
inherit autotools pkgconfig lib_package

DEPENDS = "xen-tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d6058eb45f5694b85b31237bef240081"

SRCREV_FORMAT = "source_patchqueue"
SRCREV_source = "ef1f6b10e0b217b534959affc0642b91e0b357b8"
SRCREV_patchqueue = "810cd3d577005e8759bc4ea11a7f07c93cc80812"

PV = "0+git${SRCPV}"

SRC_URI = "git://github.com/openxt/xenaccess.git;protocol=https;name=source \
	   git://github.com/openxt/xenaccess-pq.git;protocol=https;destsuffix=patchqueue;name=patchqueue"

PARALLEL_MAKE = ""

S = "${WORKDIR}/git/libxa"

do_apply_patchqueue_prepend() {
	rm -f ${WORKDIR}/git/.gitignore
}

do_install_append() {
	mv ${D}/usr/lib/libxenaccess-0.5.so ${D}/usr/lib/libxenaccess.so.0.5
	ln -sf libxenaccess.so.0.5 ${D}/usr/lib/libxenaccess.so
}
