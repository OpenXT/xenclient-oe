SUMMARY = "OpenXT OCAML libraries."
DESCRIPTION = "Set of OCAML libraries provided for OpenXT OCAML tools."
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"

DEPENDS += " \
    ocaml-dbus \
    camomile \
    xen-tools \
    xz \
"

PV = "0+git${SRCPV}"

SRC_URI = "git://github.com/OpenXT/toolstack.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"


inherit ocaml findlib

# ocamlc could determine this in the build-system, but does not currently.
CFLAGS:append = " -I${ocamlincdir}"
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
    for ocaml_lib in ${OCAML_INSTALL_LIBS}; do
        oe_runmake -C "${ocaml_lib}" V=1 install
    done
}

# .cma/.cmi files require the runtime environment.
INSANE_SKIP:${PN}-dev = "file-rdeps"
