require recipes-extended/xen/xen.inc
require xen-common.inc

DESCRIPTION = "Xen hypervisor libxl components"

# In OpenXT, multiple recipes are used to build Xen and its components:
# a 32-bit build of tools ; a 64-bit hypervisor ; and a separate blktap
# build to fix potentially circular dependencies with libv4v and icbinn.
#
# This recipe shares a common xen.inc with other recipes.
# PN in this recipe is "xen-libxl", rather than "xen" as xen.inc is
# written to expect, so in order to produce the expected package names
# with a "xen-" rather than "xen-libxl-" prefix, this python section
# renames the FILES_... variables defined in xen.inc.
# Most package names are defined explicitly rather than using ${PN}.

python () {
    for PKG in ['xl', 'xl-dev',
                'libxlutil', 'libxlutil-dev',
                'libxenlight', 'libxenlight-dev']:
        d.renameVar("FILES_xen-libxl-" + PKG, "FILES_xen-" + PKG)

    # After renaming a variable, it is simpler to append to it here:
    d.appendVar("FILES_xen-xl", "/etc/init.d/xen-init-dom0")
}

DEPENDS += "util-linux xen xen-blktap libnl"

SRC_URI += " file://xen-init-dom0.initscript "

PACKAGES = " \
    ${PN}-dbg \
    xen-xl \
    xen-libxl-dev \
    xen-libxlutil \
    xen-libxlutil-dev \
    xen-libxenlight \
    xen-libxenlight-dev \
    xen-libxl-staticdev \
    "

FILES_${PN}-staticdev = " \
    ${libdir}/libxlutil.a \
    ${libdir}/libxenlight.a \
    "

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

INITSCRIPT_PACKAGES = "xen-xl"
INITSCRIPT_NAME_xen-xl = "xen-init-dom0"
INITSCRIPT_PARAMS_xen-xl = "defaults 21"

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
