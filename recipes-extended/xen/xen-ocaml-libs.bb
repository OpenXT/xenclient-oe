require recipes-extended/xen/xen.inc
require xen-common.inc

inherit ocaml findlib

DESCRIPTION = "Xen hypervisor ocaml libs and xenstore components"

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
    blktap3 \
    libnl \
    "

RDEPENDS_${PN}-base_remove = "\
    ${PN}-blktap \
    ${PN}-libblktapctl \
    ${PN}-libvhd \
    "

RRECOMMENDS_${PN}-base_remove = " \
    ${PN}-libblktap \
    "

EXTRA_OECONF_remove = "--disable-ocamltools"

SRC_URI_append = " \
    file://xenstored.initscript \
    file://oxenstored.conf \
    "

PACKAGES = " \
    xen-xenstored-ocaml \
    ${PN}-dev \
    ${PN}-dbg \
    ${PN}-staticdev \
    ${PN} \
    "

PACKAGES_remove = " \
    ${PN}-blktap \
    ${PN}-libblktap \
    ${PN}-libblktapctl \
    ${PN}-libblktapctl-dev \
    ${PN}-libblktap-dev \
    "

FILES_${PN}-dev = "${ocamllibdir}/*/*.so"
FILES_${PN}-dbg += "${ocamllibdir}/*/.debug/*"
FILES_${PN}-staticdev = "${ocamllibdir}/*/*.a"
FILES_${PN} = "${ocamllibdir}/*"

CFLAGS_prepend += " -I${STAGING_INCDIR}/blktap "

EXTRA_OEMAKE += "CROSS_SYS_ROOT=${STAGING_DIR_HOST} CROSS_COMPILE=${HOST_PREFIX}"
EXTRA_OEMAKE += "CONFIG_IOEMU=n"
EXTRA_OEMAKE += "DESTDIR=${D}"
# OCAMLDESTDIR is set to $DESTDIR/$(ocamlfind printconf destdir), yet DESTDIR
# is required for other binaries installation, so override OCAMLDESTDIR.
EXTRA_OEMAKE += "OCAMLDESTDIR=${D}${sitelibdir}"

TARGET_CC_ARCH += "${LDFLAGS}"
CC_FOR_OCAML="i686-oe-linux-gcc"

INITSCRIPT_PACKAGES = "xen-xl xen-xenstored-ocaml"
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

do_compile() {
    oe_runmake -C tools subdir-all-include
    oe_runmake LDLIBS_libxenctrl='-lxenctrl' \
		       LDLIBS_libxenstore='-lxenstore' \
		       LDLIBS_libblktapctl='-lblktapctl' \
		       LDLIBS_libxenguest='-lxenguest' \
		       LDLIBS_libxentoollog='-lxentoollog' \
		       LDLIBS_libxenevtchn='-lxenevtchn' \
		       -C tools subdir-all-libxl
    oe_runmake V=1 \
       CC="${CC_FOR_OCAML}" \
       EXTRA_CFLAGS_XEN_TOOLS="${TARGET_CC_ARCH} --sysroot=${STAGING_DIR_TARGET}" \
       LDFLAGS="${TARGET_CC_ARCH} --sysroot=${STAGING_DIR_TARGET}" \
       LDLIBS_libxenctrl='-lxenctrl' \
       LDLIBS_libxenstore='-lxenstore' \
       LDLIBS_libblktapctl='-lblktapctl' \
       LDLIBS_libxenguest='-lxenguest' \
       LDLIBS_libxentoollog='-lxentoollog' \
       LDLIBS_libxenevtchn='-lxenevtchn' \
       -C tools subdir-all-ocaml
}

do_install() {
    oe_runmake -C tools/ocaml install

    mv ${D}/usr/sbin/oxenstored ${D}/${sbindir}/xenstored.xen-xenstored-ocaml
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/xenstored.initscript \
                    ${D}${sysconfdir}/init.d/xenstored.xen-xenstored-ocaml
    rm ${D}${sysconfdir}/xen/oxenstored.conf
    install -m 0644 ${WORKDIR}/oxenstored.conf \
                    ${D}${sysconfdir}/xen/oxenstored.conf
}
