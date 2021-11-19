DESCRIPTION = "Xen hypervisor ocaml libs and xenstore components"

XEN_REL = "4.14"
XEN_BRANCH ?= "stable-${XEN_REL}"
SRCREV ?= "${AUTOREV}"

SRC_URI = " \
    git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH} \
    file://xenstored.initscript \
    file://oxenstored.conf \
    "

LIC_FILES_CHKSUM ?= "file://COPYING;md5=419739e325a50f3d7b4501338e44a4e5"

PV = "${XEN_REL}+git${SRCPV}"

S = "${WORKDIR}/git"

require recipes-extended/xen/xen.inc
require xen-common.inc

inherit ocaml findlib python3native update-rc.d

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
    ${INIT_D_DIR}/xenstored.${PN}-xenstored \
    ${sysconfdir}/xen/oxenstored.conf \
    "
RPROVIDES_${PN}-xenstored = "virtual/xenstored"

EXTRA_OECONF_remove = "--disable-ocamltools"

# OCAMLDESTDIR is set to $DESTDIR/$(ocamlfind printconf destdir), yet DESTDIR
# is required for other binaries installation, so override OCAMLDESTDIR.
EXTRA_OEMAKE += " \
    CROSS_SYS_ROOT=${STAGING_DIR_HOST} \
    CROSS_COMPILE=${HOST_PREFIX} \
    CONFIG_IOEMU=n \
    DESTDIR=${D} \
    OCAMLDESTDIR=${D}${sitelibdir} \
    "

TARGET_CC_ARCH += "${LDFLAGS}"
CC_FOR_OCAML="${TARGET_PREFIX}gcc"

INITSCRIPT_PACKAGES = "${PN}-xenstored"
INITSCRIPT_NAME_${PN}-xenstored = "xenstored"
INITSCRIPT_PARAMS_${PN}-xenstored = "defaults 05 95"

pkg_postinst_${PN}-xenstored () {
    update-alternatives --install ${sbindir}/xenstored xenstored xenstored.${PN}-xenstored 100
    update-alternatives --install ${INIT_D_DIR}/xenstored xenstored-initscript xenstored.${PN}-xenstored 100
}

pkg_prerm_${PN}-xenstored () {
    update-alternatives --remove xenstored xenstored.${PN}-xenstored
    update-alternatives --remove xenstored-initscript xenstored.${PN}-xenstored
}

do_configure() {
    do_configure_common
}

do_compile() {
    oe_runmake -C tools/libs subdir-all-toolcore
    oe_runmake -C tools/libs subdir-all-toollog
    oe_runmake -C tools/libs subdir-all-call
    oe_runmake -C tools/libs subdir-all-hypfs
    oe_runmake -C tools subdir-all-include
    oe_runmake LDLIBS_libxenctrl='-lxenctrl' \
		       LDLIBS_libxenstore='-lxenstore' \
		       LDLIBS_libxenguest='-lxenguest' \
		       LDLIBS_libxentoollog='-lxentoollog' \
		       LDLIBS_libxenevtchn='-lxenevtchn' \
		       -C tools subdir-all-libxl

    oe_runmake V=1 \
       LDLIBS_libxenctrl='-lxenctrl' \
       LDLIBS_libxenstore='-lxenstore' \
       LDLIBS_libxenguest='-lxenguest' \
       LDLIBS_libxentoollog='-lxentoollog' \
       LDLIBS_libxenevtchn='-lxenevtchn' \
       -C tools subdir-all-ocaml
}

do_install() {
    oe_runmake -C tools/ocaml install

    mv ${D}/usr/sbin/oxenstored ${D}/${sbindir}/xenstored.${PN}-xenstored
    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/xenstored.initscript \
                    ${D}${INIT_D_DIR}/xenstored.${PN}-xenstored
    rm ${D}${sysconfdir}/xen/oxenstored.conf
    install -m 0644 ${WORKDIR}/oxenstored.conf \
                    ${D}${sysconfdir}/xen/oxenstored.conf
}

INSANE_SKIP_${PN}-dev = "file-rdeps"
