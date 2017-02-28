require xen.inc

inherit autotools-brokensep gettext pkgconfig update-rc.d

SRC_URI += "file://xenstored.initscript \
	    file://xenconsoled.initscript \
"

DEPENDS += " gettext ncurses openssl python zlib seabios ipxe gmp lzo glib-2.0 iasl-native xz "
DEPENDS += "util-linux pixman libaio yajl"
# lzo2 required by libxenguest.
RDEPENDS_${PN} += "lzo bash"

PACKAGES = "${PN}-libxenstore ${PN}-libxenstore-dev ${PN}-libxenstore-dbg ${PN}-libxenstore-staticdev   \
            ${PN}-xenstore-utils ${PN}-xenstore-utils-dbg                                               \
            ${PN}-xenconsoled ${PN}-xenconsole                                                          \
	    ${PN}-xenstored                                                                             \
            ${PN}-xenctx                                                                                \
            ${PN}-xentrace                                                                              \
            ${PN}-xenpvnetboot                                                                          \
            ${PN}-flask                                                                                 \
            ${PN}-misc                                                                                  \
            ${PN} ${PN}-dbg ${PN}-doc ${PN}-dev ${PN}-staticdev                                         \
"

FILES_${PN}-xenconsole = "${libdir}/xen/bin/xenconsole"
RDEPENDS_${PN}-xenctx += "${PN}"

FILES_${PN}-xenctx = "${libdir}/xen/bin/xenctx"
RDEPENDS_${PN}-xenctx += "${PN}"

FILES_${PN}-libxenstore = "${libdir}/libxenstore.so.*"
FILES_${PN}-libxenstore-dev = "${libdir}/libxenstore.so \
                               ${includedir}/xenstore*.h"
FILES_${PN}-libxenstore-dbg = "${libdir}/.debug/libxenstore.so*"
FILES_${PN}-libxenstore-staticdev = "${libdir}/libxenstore.a"

FILES_${PN}-toolstack-headers = "${includedir}/xc_dom.h            \
                                 ${includedir}/xen/libelf.h        \
                                 ${includedir}/xen/elfstructs.h"

FILES_${PN}-xenstore-utils = "${bindir}/xenstore-*"
FILES_${PN}-xenstore-utils-dbg = "${bindir}/.debug/xenstore-*"
RDEPENDS_${PN}-xenstore-utils += "${PN}-libxenstore"

FILES_${PN}-xentrace = "${datadir}/xentrace \
                        ${bindir}/xentrace_format"
RDEPENDS_${PN}-xentrace += "python"

FILES_${PN}-staticdev += "${libdir}/*.a"
FILES_${PN}-dbg += "${libdir}*/*/*/.debug           \
                    ${libdir}/*/*/.debug            \
                    ${libdir}/*/.debug"
FILES_${PN}-xenpvnetboot = "${libdir}/xen/bin/xenpvnetboot"
RDEPENDS_${PN}-xenpvnetboot += "python"
FILES_${PN}-flask = "/boot/xenpolicy-${PV}"
FILES_${PN}-misc = "${sbindir}/xen-ringwatch \
                    ${bindir}/xencons \
                    ${sbindir}/xen-bugtool \
                    ${sbindir}/xencov \
                    ${bindir}/xencov_split"
RDEPENDS_${PN}-misc += "perl python"

FILES_${PN} += "${datadir}/xen/qemu"
RDEPENDS_${PN} += "${PN}-xenstore-utils"

FILES_${PN}-xenstored = "${sysconfdir}/init.d/xenstored ${sbindir}/xenstored /*/*/xenstored"
FILES_${PN}-xenconsoled = "${sysconfdir}/init.d/xenconsoled ${sbindir}/xenconsoled /*/*/xenconsoled"
INITSCRIPT_PACKAGES = "${PN}-xenconsoled"
INITSCRIPT_NAME_${PN}-xenconsoled = "xenconsoled"
INITSCRIPT_PARAMS_${PN} = "defaults 60"

INITSCRIPT_NAME_${PN}-xenstored = "xenstored"
INITSCRIPT_PARAMS_${PN}-xenstored = "defaults 05"

EXTRA_OECONF += " \
    --prefix=${prefix} \
    --libexecdir=${libdir} \
    --host=${HOST_SYS} \
    --disable-xen \
    --disable-stubdom \
    --disable-ioemu-stubdom \
    --disable-pv-grub \
    --disable-xenstore-stubdom \
    --disable-rombios \
    --disable-ocamltools \
    --with-initddir=${INIT_D_DIR} \
    --with-sysconfig-leaf-dir=default \
    --with-system-qemu=/usr/bin/qemu-system-i386 \
    --disable-qemu-traditional \
"

EXTRA_OEMAKE += "CROSS_SYS_ROOT=${STAGING_DIR_HOST} CROSS_COMPILE=${HOST_PREFIX}"
EXTRA_OEMAKE += "CONFIG_IOEMU=n"
EXTRA_OEMAKE += "DESTDIR=${D}"

TARGET_CC_ARCH += "${LDFLAGS}"

do_configure() {
	oe_runconf 
}

do_compile() {
        oe_runmake -C tools subdir-all-include
        oe_runmake -C tools subdir-all-libxc
        oe_runmake -C tools subdir-all-flask
        oe_runmake -C tools subdir-all-xenstore
        oe_runmake -C tools subdir-all-misc
        oe_runmake -C tools subdir-all-hotplug
        oe_runmake -C tools subdir-all-xentrace
        oe_runmake -C tools subdir-all-xenmon
        oe_runmake -C tools subdir-all-console
        oe_runmake -C tools subdir-all-xenstat
}

do_install() {
        oe_runmake DESTDIR=${D} -C tools subdir-install-include
        oe_runmake DESTDIR=${D} -C tools subdir-install-libxc
        oe_runmake DESTDIR=${D} -C tools subdir-install-flask
        oe_runmake DESTDIR=${D} -C tools subdir-install-xenstore
        oe_runmake DESTDIR=${D} -C tools subdir-install-misc
        oe_runmake DESTDIR=${D} -C tools subdir-install-hotplug
        oe_runmake DESTDIR=${D} -C tools subdir-install-xentrace
        oe_runmake DESTDIR=${D} -C tools subdir-install-xenmon
        oe_runmake DESTDIR=${D} -C tools subdir-install-console
        oe_runmake DESTDIR=${D} -C tools subdir-install-xenstat

# Should not be necessary anymore
        rm -rf ${D}/etc/udev
        find ${D} -name "xm" -delete
        find ${D} -name "*xend*" -delete
        rm -f ${D}/usr/sbin/tapdisk ${D}/usr/sbin/blktapctrl
        rm -f ${D}/etc/xen/scripts/block

        install -d ${D}${sysconfdir}/init.d
	rm -f ${D}/etc/init.d/xencommons
	rm -f ${D}/etc/init.d/xen-watchdog
        install -m 0755 ${WORKDIR}/xenstored.initscript ${D}${sysconfdir}/init.d/xenstored
        install -m 0755 ${WORKDIR}/xenconsoled.initscript ${D}${sysconfdir}/init.d/xenconsoled

 # Export local headers for external toolstack
        install -d ${D}${includedir}/xen/libelf
        install -m 0755 ${S}/tools/libxc/include/xc_dom.h ${D}${includedir}/xc_dom.h
        install -m 0755 ${S}/tools/include/xen/libelf/libelf.h ${D}${includedir}/xen/libelf/libelf.h
        install -m 0755 ${S}/tools/include/xen/libelf/elfstructs.h ${D}${includedir}/xen/libelf/elfstructs.h
}

