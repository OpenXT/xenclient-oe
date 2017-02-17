require xen.inc

inherit pkgconfig pythonnative update-rc.d

DEPENDS += "util-linux xen-tools xen-blktap libnl"

SRC_URI += " file://xen-init-dom0.initscript "

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

INITSCRIPT_NAME = "xen-init-dom0"
INITSCRIPT_PARAMS = "defaults 21"

FILES_${PN} += " /usr/lib/xen/bin "
FILES_${PN}-dbg += " /usr/lib/xen/bin/.debug "

do_configure() {
        DESTDIR=${D} ./configure --prefix=${prefix}
}

do_configure_prepend() {
	#remove optimizations in the config files
	sed -i 's/-O2//g' ${WORKDIR}/xen-${XEN_VERSION}/Config.mk
	sed -i 's/-O2//g' ${WORKDIR}/xen-${XEN_VERSION}/config/StdGNU.mk
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
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/xen-init-dom0.initscript ${D}${sysconfdir}/init.d/xen-init-dom0
}
