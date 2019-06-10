require recipes-extended/xen/xen.inc
require xen-common.inc

DESCRIPTION = "Xen hypervisor blktap2 and libvhd components"

# In OpenXT, multiple recipes are used to build Xen and its components:
# a 32-bit build of tools ; a 64-bit hypervisor ; and a separate blktap
# build to fix potentially circular dependencies with libargo and icbinn.
#
# This recipe shares a common xen.inc with other recipes.
# PN in this recipe is "xen-blktap", rather than "xen" as xen.inc is
# written to expect, so in order to produce the expected package names
# with a "xen-" rather than "xen-blktap-" prefix, this python section
# renames the FILES_... variables defined in xen.inc.
# Most package names are defined explicitly rather than using ${PN}.

python () {
    for PKG in ['blktap',
                'libblktap',
                'libblktap-dev',
                'libblktapctl',
                'libblktapctl-dev',
                'libvhd',
                'libvhd-dev']:
        d.renameVar("FILES_xen-blktap-" + PKG, "FILES_xen-" + PKG)
}

DEPENDS += "util-linux xen openssl libaio libicbinn-resolved"

PACKAGES = " \
    ${PN}-dbg \
    xen-blktap \
    xen-blktap-dev \
    xen-libblktap \
    xen-libblktapctl \
    xen-libblktapctl-dev \
    xen-libblktap-dev \
    xen-libvhd \
    xen-libvhd-dev \
    xen-blktap-staticdev \
    "

FILES_${PN}-staticdev = " \
    ${libdir}/libblktapctl.a \
    ${libdir}/libblktap.a \
    ${libdir}/libvhd.a \
    "

do_compile() {
    oe_runmake -C tools subdir-all-include
    oe_runmake -C tools subdir-all-blktap2
}

do_install() {
    install -d ${D}${datadir}/pkgconfig
    oe_runmake DESTDIR=${D} -C tools subdir-install-blktap2
    install -d ${D}/usr/include
    install tools/blktap2/control/tap-ctl.h ${D}/usr/include
    install tools/blktap2/include/tapdisk-message.h ${D}/usr/include

    # /usr/share is not packaged, removing to silence QA warnings
    rm -rf ${D}/${datadir}
}

RDEPENDS_${PN} += "glibc-gconv-utf-16"
RCONFLICTS_xen-blktap = "blktap3"
RCONFLICTS_xen-blktapctl = "blktap3"
RCONFLICTS_xen-libvhd = "blktap3"
