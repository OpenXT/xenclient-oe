require recipes-extended/xen/xen.inc
require xen-common.inc

inherit findlib

DESCRIPTION = "Xen hypervisor libxl, ocaml and xenstore components"

# In OpenXT, multiple recipes are used to build Xen and its components:
# a 32-bit build of tools ; a 64-bit hypervisor ; a separate blktap
# build to fix potentially circular dependencies with libv4v and icbinn
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

# OpenXT packages both the C and OCaml versions of XenStored.
# This recipe packages the OCaml daemon; xen.bb packages the C one.
FILES_xen-xenstored-ocaml = " \
    ${sbindir}/xenstored.xen-xenstored-ocaml \
    ${localstatedir}/lib/xenstored \
    ${sysconfdir}/init.d/xenstored.xen-xenstored-ocaml \
    ${sysconfdir}/xen/oxenstored.conf \
    "

PROVIDES =+ "xen-xenstored xen-xenstored-ocaml"
RPROVIDES_xen-xenstored-ocaml = "xen-xenstored xen-xenstored-ocaml"

DEPENDS += " \
    util-linux \
    xen \
    xen-blktap \
    libnl \
    ocaml-cross \
    "
EXTRA_OECONF_remove = "--disable-ocamltools"

SRC_URI_append = " \
    file://xen-init-dom0.initscript \
    file://xenstored.initscript \
    file://oxenstored.conf \
    "

PACKAGES = " \
    xen-xl \
    xen-libxl-dev \
    xen-libxlutil \
    xen-libxlutil-dev \
    xen-libxenlight \
    xen-libxenlight-dev \
    xen-libxl-staticdev \
    xen-xenstored-ocaml \
    xen-ocaml-libs-dev \
    xen-ocaml-libs-dbg \
    xen-ocaml-libs-staticdev \
    xen-ocaml-libs \
    ${PN}-dbg \
    "

FILES_${PN}-staticdev = " \
    ${libdir}/libxlutil.a \
    ${libdir}/libxenlight.a \
    "

FILES_xen-ocaml-libs-dev = "${ocamllibdir}/*/*.so"
FILES_xen-ocaml-libs-dbg = "${ocamllibdir}/*/.debug/*"
FILES_xen-ocaml-libs-staticdev = "${ocamllibdir}/*/*.a"
FILES_xen-ocaml-libs = "${ocamllibdir}/*"

EXTRA_OEMAKE += "CROSS_SYS_ROOT=${STAGING_DIR_HOST} CROSS_COMPILE=${HOST_PREFIX}"
EXTRA_OEMAKE += "CONFIG_IOEMU=n"
EXTRA_OEMAKE += "CONFIG_TESTS=n"
EXTRA_OEMAKE += "DESTDIR=${D}"

#Make sure we disable all compiler optimizations to avoid a nasty segfault in the 
#reboot case.
BUILD_LDFLAGS += " -Wl,-O0 -O0"
BUILDSDK_LDFLAGS += " -Wl,-O0 -O0"
TARGET_LDFLAGS += " -Wl,-O0 -O0"
BUILD_OPTIMIZATION = "-pipe"
FULL_OPTIMIZATION = "-pipe ${DEBUG_FLAGS}"

TARGET_CC_ARCH += "${LDFLAGS}"
CC_FOR_OCAML="i686-oe-linux-gcc"

INITSCRIPT_PACKAGES = "xen-xl xen-xenstored-ocaml"
INITSCRIPT_NAME_xen-xl = "xen-init-dom0"
INITSCRIPT_PARAMS_xen-xl = "defaults 21"
INITSCRIPT_NAME_xen-xenstored-ocaml = "xenstored"
INITSCRIPT_PARAMS_xen-xenstored-ocaml = "defaults 05"

pkg_postinst_xen-xenstored-ocaml () {
    update-alternatives --install ${sbindir}/xenstored xenstored xenstored.xen-xenstored-ocaml 100
    update-alternatives --install ${sysconfdir}/init.d/xenstored xenstored-initscript xenstored.xen-xenstored-ocaml 100
}

pkg_prerm_xen-xenstored-ocaml () {
    update-alternatives --remove xenstored xenstored.xen-xenstored-ocaml
    update-alternatives --remove xenstored-initscript xenstored.xen-xenstored-ocaml
}

do_configure_prepend() {
	#remove optimizations in the config files
	sed -i 's/-O2//g' ${WORKDIR}/xen-${XEN_VERSION}/Config.mk
	sed -i 's/-O2//g' ${WORKDIR}/xen-${XEN_VERSION}/config/StdGNU.mk
}

do_compile() {
    oe_runmake -C tools subdir-all-include
    oe_runmake LDLIBS_libxenctrl='-lxenctrl' \
		       LDLIBS_libxenstore='-lxenstore' \
		       LDLIBS_libblktapctl='-lblktapctl' \
		       LDLIBS_libxenguest='-lxenguest' \
		       -C tools subdir-all-libxl
    oe_runmake V=1 \
       CC="${CC_FOR_OCAML}" \
       EXTRA_CFLAGS_XEN_TOOLS="${TARGET_CC_ARCH} --sysroot=${STAGING_DIR_TARGET}" \
       LDFLAGS="${TARGET_CC_ARCH} --sysroot=${STAGING_DIR_TARGET}" \
       LDLIBS_libxenctrl='-lxenctrl' \
       LDLIBS_libxenstore='-lxenstore' \
       LDLIBS_libblktapctl='-lblktapctl' \
       LDLIBS_libxenguest='-lxenguest' \
       -C tools subdir-all-ocaml
}

do_install() {
    oe_runmake DESTDIR=${D} -C tools subdir-install-libxl
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/xen-init-dom0.initscript \
                    ${D}${sysconfdir}/init.d/xen-init-dom0

    oe_runmake DESTDIR=${D} -C tools/ocaml install

    mv ${D}/usr/sbin/oxenstored ${D}/${sbindir}/xenstored.xen-xenstored-ocaml
    install -m 0755 ${WORKDIR}/xenstored.initscript \
                    ${D}${sysconfdir}/init.d/xenstored.xen-xenstored-ocaml
    rm ${D}${sysconfdir}/xen/oxenstored.conf
    install -m 0644 ${WORKDIR}/oxenstored.conf \
                    ${D}${sysconfdir}/xen/oxenstored.conf
}
