inherit findlib
SRC_URI[md5sum] = "1e25b6cd4efd26ab38a667db18d83f02"
SRC_URI[sha256sum] = "85806b051cf059b93676a10a3f66051f7f322cad6e3248172c3e5275f79d7100"
DESCRIPTION = "Camomile, unicode library for OCAML"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d8045f3b8f929c1cb29a1e3fd737b499"

DEPENDS = "ocaml-cross ocaml-findlib-cross"

PR = "r0"

SRC_URI = "http://github.com/yoriyuki/Camomile/releases/download/rel-0.8.5/camomile-0.8.5.tar.bz2 \
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
