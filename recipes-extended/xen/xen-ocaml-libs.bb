DESCRIPTION = "Xen hypervisor ocaml libs and xenstore components"

XEN_REL ?= "4.12"
XEN_BRANCH ?= "stable-${XEN_REL}"
SRCREV ?= "${AUTOREV}"

SRC_URI = " \
    git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH} \
    file://xenstored.initscript \
    file://oxenstored.conf \
    "

LIC_FILES_CHKSUM ?= "file://COPYING;md5=bbb4b1bdc2c3b6743da3c39d03249095"

PV = "${XEN_REL}+git${SRCPV}"

S = "${WORKDIR}/git"

require recipes-extended/xen/xen.inc
require xen-common.inc

inherit ocaml findlib

PACKAGES = " \
    ${PN}-xenstored \
    ${PN}-dev \
    ${PN}-dbg \
    ${PN}-staticdev \
    ${PN} \
    "

PROVIDES =+ "virtual/xenstored"

DEPENDS += " \
    util-linux \
    xen \
    libnl \
    xen-tools \
    "

# OpenXT packages both the C and OCaml versions of XenStored.
# This recipe packages the OCaml daemon; xen.bb packages the C one.
FILES_${PN}-xenstored = " \
    ${sbindir}/xenstored.${PN}-xenstored \
    ${localstatedir}/lib/xenstored \
    ${sysconfdir}/init.d/xenstored.${PN}-xenstored \
    ${sysconfdir}/xen/oxenstored.conf \
    "
RPROVIDES_${PN}-xenstored = "virtual/xenstored"

EXTRA_OECONF_remove = "--disable-ocamltools"

CFLAGS_prepend += " -I${STAGING_INCDIR}/blktap "

# OCAMLDESTDIR is set to $DESTDIR/$(ocamlfind printconf destdir), yet DESTDIR
# is required for other binaries installation, so override OCAMLDESTDIR.
EXTRA_OEMAKE += " \
    CROSS_SYS_ROOT=${STAGING_DIR_HOST} \
    CROSS_COMPILE=${HOST_PREFIX} \
    CONFIG_IOEMU=n \
    DESTDIR=${D} \
    OCAMLDESTDIR=${D}${sitelibdir} \
    "

EXTRA_OECONF += " --enable-blktap2 "

TARGET_CC_ARCH += "${LDFLAGS}"
CC_FOR_OCAML="${TARGET_PREFIX}gcc"

INITSCRIPT_PACKAGES = "${PN}-xenstored"
INITSCRIPT_NAME_${PN}-xenstored = "xenstored"
INITSCRIPT_PARAMS_${PN}-xenstored = "defaults 05"

pkg_postinst_${PN}-xenstored () {
    update-alternatives --install ${sbindir}/xenstored xenstored xenstored.${PN}-xenstored 100
    update-alternatives --install ${sysconfdir}/init.d/xenstored xenstored-initscript xenstored.${PN}-xenstored 100
}

pkg_prerm_${PN}-xenstored () {
    update-alternatives --remove xenstored xenstored.${PN}-xenstored
    update-alternatives --remove xenstored-initscript xenstored.${PN}-xenstored
}

do_configure() {
    do_configure_common
}

do_compile() {
    export EXTRA_CFLAGS_XEN_TOOLS="-I${STAGING_INCDIR}/blktap ${EXTRA_CFLAGS_XEN_TOOLS}"

    oe_runmake -C tools/libs subdir-all-toolcore
    oe_runmake -C tools subdir-all-include
    oe_runmake LDLIBS_libxenctrl='-lxenctrl' \
		       LDLIBS_libxenstore='-lxenstore' \
		       LDLIBS_libblktapctl='-lblktapctl' \
		       LDLIBS_libxenguest='-lxenguest' \
		       LDLIBS_libxentoollog='-lxentoollog' \
		       LDLIBS_libxenevtchn='-lxenevtchn' \
		       -C tools subdir-all-libxl

    oe_runmake V=1 \
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

    mv ${D}/usr/sbin/oxenstored ${D}/${sbindir}/xenstored.${PN}-xenstored
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/xenstored.initscript \
                    ${D}${sysconfdir}/init.d/xenstored.${PN}-xenstored
    rm ${D}${sysconfdir}/xen/oxenstored.conf
    install -m 0644 ${WORKDIR}/oxenstored.conf \
                    ${D}${sysconfdir}/xen/oxenstored.conf
}

INSANE_SKIP_${PN}-dev = "file-rdeps"
