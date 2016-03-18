DESCRIPTION = "blktap"
LICENSE = "GPLv2"
DEPENDS = "openssl xen-tools libaio util-linux libicbinn-resolved"
LIC_FILES_CHKSUM="file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

RDEPENDS_${PN} += "glibc-gconv-utf-16 bash"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/blktap.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S = "${WORKDIR}/git"

# Makefile doesn't generate hash in libs.. todo: check that all is ok
INSANE_SKIP_${PN} = "1"

CFLAGS += "-DVHD_LOCKING -Wno-maybe-uninitialized"
EXTRA_OEMAKE += "CROSS_COMPILE=${HOST_PREFIX}"

inherit xenclient

do_compile() {
	oe_runmake USE_SYSTEM_LIBRARIES=y XEN_ROOT=${STAGING_INCDIR} CROSS_COMPILE=${HOST_PREFIX} BLKTAP_TARGET_ARCH=x86_32
}

do_install() {
        mkdir -p ${S}/dist
	oe_runmake DESTDIR=${S}/dist USE_SYSTEM_LIBRARIES=y XEN_ROOT=${STAGING_INCDIR} \
		   CROSS_COMPILE=${HOST_PREFIX} BLKTAP_TARGET_ARCH=x86_32 \
		   LIBDIR=/usr/lib BINDIR=/usr/bin SBINDIR=/usr/sbin SYSCONFDIR=/etc install
	oe_runmake DESTDIR=${D} USE_SYSTEM_LIBRARIES=y XEN_ROOT=${STAGING_INCDIR} \
		   CROSS_COMPILE=${HOST_PREFIX} BLKTAP_TARGET_ARCH=x86_32 \
		   LIBDIR=/usr/lib BINDIR=/usr/bin SBINDIR=/usr/sbin SYSCONFDIR=/etc install
}

#do_stage() {
#        cp -r ${S}/dist/usr ${STAGING_DIR_HOST}
#}
