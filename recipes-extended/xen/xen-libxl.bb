require recipes-extended/xen/xen.inc
require xen-common.inc

DESCRIPTION = "Xen hypervisor libxl components"

# In OpenXT, multiple recipes are used to build Xen and its components:
# a 32-bit build of tools ; a 64-bit hypervisor ; a separate blktap
# build to fix potentially circular dependencies with libargo and icbinn
# and the remainder.
#
# This recipe shares a common xen.inc with other recipes.
# PN in this recipe is "xen-libxl", rather than "xen" as xen.inc is
# written to expect, so in order to produce the expected package names
# with a "xen-" rather than "xen-libxl-" prefix, this python section
# renames the FILES_... variables defined in xen.inc.
# Most package names are defined explicitly rather than using ${PN}.

python () {
    for PKG in ['xl',
                'xl-dev',
                'libxlutil',
                'libxlutil-dev',
                'libxenlight',
                'libxenlight-dev'
                ]:
        d.renameVar("FILES_xen-libxl-" + PKG, "FILES_xen-" + PKG)

    # After renaming a variable, it is simpler to append to it here:
    d.appendVar("FILES_xen-xl", " /etc/init.d/xen-init-dom0")
}

FLASK_POLICY_FILE = "xenpolicy-${XEN_PV}"

DEPENDS += " \
    util-linux \
    xen \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', 'xen-blktap', 'blktap3', d)} \
    libnl \
    "

RDEPENDS_${PN}-base_remove = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', '${PN}-blktap ${PN}-libblktapctl ${PN}-libvhd', d)} \
    "

RRECOMMENDS_${PN}-base_remove = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', '${PN}-libblktap', d)} \
    "

SRC_URI_append = " \
    file://xen-init-dom0.initscript \
    file://xl.conf \
    "

PACKAGES = " \
    xen-xl \
    xen-libxl-dev \
    xen-libxlutil \
    xen-libxlutil-dev \
    xen-libxenlight \
    xen-libxenlight-dev \
    xen-libxl-staticdev \
    ${PN}-dbg \
    "

PACKAGES_remove = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', '${PN}-blktap ${PN}-libblktap ${PN}-libblktapctl ${PN}-libblktapctl-dev ${PN}-libblktap-dev', d)} \
    "

FILES_${PN}-staticdev = " \
    ${libdir}/libxlutil.a \
    ${libdir}/libxenlight.a \
    "
FILES_xen-libxlutil += " \
    ${sysconfdir}/xen/xl.conf \
"
FILES_${PN}-dev += " \
    ${includedir} \
"
FILES_${PN}-dbg += " \
    ${bindir}/.debug \
    ${sbindir}/.debug \
    ${libdir}/.debug \
    /usr/src/debug \
"

CFLAGS_prepend += "${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', '-I${STAGING_INCDIR}/blktap',d)}"

EXTRA_OEMAKE += "CROSS_SYS_ROOT=${STAGING_DIR_HOST} CROSS_COMPILE=${HOST_PREFIX}"
EXTRA_OEMAKE += "CONFIG_IOEMU=n"
EXTRA_OEMAKE += "CONFIG_TESTS=n"
EXTRA_OEMAKE += "DESTDIR=${D}"

EXTRA_OECONF += " --enable-blktap2 --with-system-ipxe=/usr/share/firmware/82540em.rom "

#Make sure we disable all compiler optimizations to avoid a nasty segfault in the 
#reboot case.
BUILD_LDFLAGS += " -Wl,-O0 -O0"
BUILDSDK_LDFLAGS += " -Wl,-O0 -O0"
TARGET_LDFLAGS += " -Wl,-O0 -O0"
BUILD_OPTIMIZATION = "-pipe"
FULL_OPTIMIZATION = "-pipe ${DEBUG_FLAGS}"

TARGET_CC_ARCH += "${LDFLAGS}"
CC_FOR_OCAML="${TARGET_PREFIX}gcc"

INITSCRIPT_PACKAGES = "xen-xl"
INITSCRIPT_NAME_xen-xl = "xen-init-dom0"
INITSCRIPT_PARAMS_xen-xl = "defaults 21"

do_configure_prepend() {
	#remove optimizations in the config files
	sed -i 's/-O2//g' ${S}/Config.mk
	sed -i 's/-O2//g' ${S}/config/StdGNU.mk

	cp "${WORKDIR}/defconfig" "${B}/xen/.config"
}

do_compile() {
    oe_runmake -C tools/libs subdir-all-toolcore
    oe_runmake -C tools subdir-all-include
    oe_runmake LDLIBS_libxenctrl='-lxenctrl' \
		       LDLIBS_libxenstore='-lxenstore' \
		       LDLIBS_libblktapctl='-lblktapctl' \
		       LDLIBS_libxenguest='-lxenguest' \
		       LDLIBS_libxentoollog='-lxentoollog' \
		       LDLIBS_libxenevtchn='-lxenevtchn' \
		       -C tools subdir-all-libxl
    oe_runmake LDLIBS_libxenctrl='-lxenctrl' \
		       LDLIBS_libxenstore='-lxenstore' \
		       LDLIBS_libblktapctl='-lblktapctl' \
		       LDLIBS_libxenguest='-lxenguest' \
		       LDLIBS_libxentoollog='-lxentoollog' \
		       LDLIBS_libxenevtchn='-lxenevtchn' \
		       -C tools subdir-all-xl
    oe_runmake LDLIBS_libxenctrl='-lxenctrl' \
		       LDLIBS_libxenstore='-lxenstore' \
		       LDLIBS_libblktapctl='-lblktapctl' \
		       LDLIBS_libxenguest='-lxenguest' \
		       LDLIBS_libxentoollog='-lxentoollog' \
		       LDLIBS_libxenevtchn='-lxenevtchn' \
		       -C tools subdir-all-helpers
}

do_install() {
    install -d ${D}${datadir}/pkgconfig
    oe_runmake DESTDIR=${D} -C tools subdir-install-libxl
    oe_runmake DESTDIR=${D} -C tools subdir-install-xl
    oe_runmake DESTDIR=${D} -C tools subdir-install-helpers
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/xen-init-dom0.initscript \
                    ${D}${sysconfdir}/init.d/xen-init-dom0
    install -d ${D}${sysconfdir}/xen
    install -m 0644 ${WORKDIR}/xl.conf \
                    ${D}${sysconfdir}/xen/xl.conf

    # Since we don't have a xenstore stubdomain, remove the
    # xenstore stubdomain init program (libdir == /usr/lib)
    rm -f ${D}/${libdir}/xen/bin/init-xenstore-domain
}
