require xen.inc

inherit pkgconfig pythonnative

DEPENDS += "util-linux xen-tools xen-blktap"

EXTRA_OEMAKE += "CROSS_SYS_ROOT=${STAGING_DIR_HOST} CROSS_COMPILE=${HOST_PREFIX}"
EXTRA_OEMAKE += "CONFIG_IOEMU=n"
EXTRA_OEMAKE += "DESTDIR=${D}"

#Make sure we disable all compiler optimizations to avoid a nasty segfault in the 
#reboot case.
BUILD_LDFLAGS += " -Wl,-O0 -O0"
BUILDSDK_LDFLAGS += " -Wl,-O0 -O0"
TARGET_LDFLAGS += " -Wl,-O0 -O0"
BUILD_OPTIMIZATION = "-pipe"
FULL_OPTIMIZATION = "-pipe ${DEBUG_FLAGS}"

TARGET_CC_ARCH += "${LDFLAGS}"
do_configure() {
        DESTDIR=${D} ./configure --prefix=${prefix}
}

do_configure_prepend() {
	#remove optimizations in the config files
	sed -i 's/-O2//g' ${WORKDIR}/xen-4.6.1/Config.mk
	sed -i 's/-O2//g' ${WORKDIR}/xen-4.6.1/config/StdGNU.mk
}

do_compile() {
        oe_runmake -C tools/include xen-xsm/.dir
        oe_runmake LDLIBS_libxenctrl='-lxenctrl' \
		   LDLIBS_libxenstore='-lxenstore' \
		   LDLIBS_libblktapctl='-lblktapctl' \
		   LDLIBS_libxenguest='-lxenguest' \
		   -C tools subdir-all-libxl
}

do_install() {
        oe_runmake DESTDIR=${D} -C tools subdir-install-libxl
}
