inherit findlib
SRC_URI[md5sum] = "999fc48a71030a1e765b85e3c1e7b933"
SRC_URI[sha256sum] = "ca3c50f5442fce0ec9a9d35bfa99ab78084a2b5b02638c6e95f38340f9f104e5"
DESCRIPTION = "Camomile, unicode library for OCAML"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d8045f3b8f929c1cb29a1e3fd737b499"

DEPENDS = "ocaml-cross ocaml-findlib-cross"

PR = "r0"

SRC_URI = "http://downloads.sourceforge.net/project/camomile/camomile/0.8.1/camomile-0.8.1.tar.bz2 \
           file://ocaml-camomile-destdir.patch;patch=1 \
"
#

S = "${WORKDIR}/camomile-${PV}"

FILES_${PN} = "${ocamllibdir}/camomile/*${SOLIBS}   \
               /usr/share/camomile/charmaps/*       \
               /usr/share/camomile/database/*       \
               /usr/share/camomile/locales/*        \
               /usr/share/camomile/mappings/*       \
               "
FILES_${PN}-dev = "${ocamllibdir}/camomile/*${SOLIBSDEV}    \
                   ${ocamllibdir}/camomile/*.cm*            \
                   ${ocamllibdir}/camomile/META             \
                   "
FILES_${PN}-staticdev = "${ocamllibdir}/camomile"
FILES_${PN}-dbg = "${ocamllibdir}/camomile/.debug/*"

do_configure() {
    ./configure --prefix=${prefix} --bindir=${bindir} --libdir=${libdir} --datadir=${datadir}
}

do_compile() {
    make \
        OCAMLC="ocamlc -cc '${CC}'" \
        OCAMLOPT="ocamlopt -cc '${CC}'"
}

do_install() {
    make install DESTDIR="${D}"
}

#do_stage() {
#    make install
#}
