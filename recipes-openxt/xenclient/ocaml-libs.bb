DESCRIPTION = "OpenXT OCaml libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS += "ocaml-cross ocaml-dbus ocaml-camomile xen xz"

inherit autotools-brokensep findlib xenclient

PACKAGES = "${PN}-dbg ${PN}-staticdev ${PN}-dev ${PN}"

FILES_${PN}-dbg += "${ocamllibdir}/*/.debug/*"
FILES_${PN}-dev = "${ocamllibdir}/*/*.so"
FILES_${PN}-staticdev = "${ocamllibdir}/*/*.a"
FILES_${PN} = "${ocamllibdir}/*"

# Ocaml stuff is built with the native compiler with "-m32".
CFLAGS_append = " -I${OCAML_HEADERS}"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/toolstack.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"
S = "${WORKDIR}/git"

OCAML_INSTALL_LIBS = " \
    libs/uuid \
    libs/stdext \
    libs/json \
    libs/jsonrpc \
    libs/http \
    libs/log \
    libs/common \
    "

do_configure() {
        :
}

do_compile() {
        make V=1 XEN_DIST_ROOT="${STAGING_DIR}"
}

do_install() {
        make DESTDIR=${D} V=1 install

        # install ocaml libraries required by other packages
        mkdir -p "${D}${ocamllibdir}"
        for ocaml_lib in ${OCAML_INSTALL_LIBS}
        do
                oe_runmake -C $ocaml_lib DESTDIR=${D} V=1 install || exit 1
        done
}
