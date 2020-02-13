DESCRIPTION = "XenClient toolstack"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "ocaml-dbus camomile xen xz"

PV = "0+git${SRCPV}"

SRC_URI = " \
    git://${OPENXT_GIT_MIRROR}/toolstack.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
    git://github.com/OpenXT/toolstack.git \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', 'file://0001-blktap3-move-physical-device-xenstore-node-creation-.patch', d)} \
    file://vif \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit ocaml findlib

# TODO: ocamlc can figure it out in the build-system.
CFLAGS_append = " -I${ocamlincdir}"
do_compile() {
    oe_runmake V=1 XEN_DIST_ROOT="${STAGING_DIR_HOST}"
}

OCAML_INSTALL_LIBS = " \
    libs/uuid \
    libs/stdext \
    libs/json \
    libs/jsonrpc \
    libs/http \
    libs/log \
    libs/common \
"

do_install() {
    oe_runmake DESTDIR=${D} V=1 install
    rm -f ${D}${sysconfdir}/xen/scripts/vif

    install -d ${D}${sysconfdir}/xen/scripts
    install -m 0755 ${WORKDIR}/vif ${D}/etc/xen/scripts/vif

    for ocaml_lib in ${OCAML_INSTALL_LIBS}; do
        # Use of DESTDIR is not consistent here.
        # root Makefile sur $(DESTDIR)/usr/bin while libs use
        # $(DESTDIR)/$(ocamlfind printconf destdir)
        oe_runmake -C "${ocaml_lib}" V=1 install
    done
}

PACKAGES =+ " \
    ${PN}-block-scripts \
    ${PN}-vif-scripts \
    ${PN}-libs-dbg \
    ${PN}-libs-staticdev \
    ${PN}-libs-dev \
    ${PN}-libs \
"

FILES_${PN}-block-scripts += " \
    ${sysconfdir}/xen/scripts/block \
    ${sysconfdir}/xen/scripts/tap \
    ${sysconfdir}/udev/rules.d/xen-block-backend.rules \
    ${sysconfdir}/udev/rules.d/xen-tap-backend.rules \
"
FILES_${PN}-vif-scripts += " \
    ${sysconfdir}/xen/scripts/vif \
"

FILES_${PN}-libs-dbg += " \
    ${sitelibdir}/*/.debug \
"
FILES_${PN}-libs-staticdev += " \
    ${sitelibdir}/*/*.a \
"
FILES_${PN}-libs-dev += " \
    ${sitelibdir}/*/*.cm* \
    ${sitelibdir}/*/*.mli \
    ${sitelibdir}/*/META \
"
FILES_${PN}-libs += " \
    ${sitelibdir}/*/*${SOLIBSDEV} \
"

FILES_${PN} += " \
    ${sysconfdir} \
"
RDEPENDS_${PN} = " \
    xen-xenstore \
    xen-xenstored \
"

# .cma/.cmi files require the runtime environment.
INSANE_SKIP_${PN}-dev = "file-rdeps"
INSANE_SKIP_${PN}-libs-dev = "file-rdeps"
