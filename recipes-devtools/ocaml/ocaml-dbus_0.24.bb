inherit findlib pkgconfig
SRC_URI[md5sum] = "b769af9141a5c073056ed46ef76ba5be"
SRC_URI[sha256sum] = "7c793987668e4236c63857469d2abe4a460e0b0954aa7d3262c6d9bb3c24bfdd"
DESCRIPTION = "OCaml DBUS bindings"
DEPENDS = "ocaml-cross ocaml-findlib-cross dbus"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f89276652d4738435c98d27fee7c6c7f"

PR = "r5"

S = "${WORKDIR}/ocaml_dbus-${PV}"

SRC_URI = "http://projects.snarc.org/ocaml-dbus/download/ocaml_dbus-${PV}.tar.bz2 \
           file://fix-invalid-characters-in-byte-access.patch;patch=1 \
           file://fix-incorrect-dispatch-statuses.patch;patch=1 \
           file://fix-error-name-lookup.patch;patch=1 \
	   file://fix-memleak.patch;patch=1 \
	   file://fix-multithread.patch;patch=1 \
"

RDEPENDS_${PN}-dev = ""

PARALLEL_MAKE = ""

do_compile() {
	oe_runmake \
		OCAMLC="ocamlc -cc '${CC}'" \
		OCAMLOPT="ocamlopt -cc '${CC}'" \
		OCAMLMKLIB="ocamlmklib -L'${STAGING_DIR_TARGET}/lib' -L'${STAGING_DIR_TARGET}/usr/lib'"

}

do_install() {
	mkdir -p ${D}${ocamllibdir}
	ocamlfind install -destdir ${D}${ocamllibdir} dbus META dBus.cmxa dBus.cma dBus.cmi dlldbus_stubs.so dBus.a libdbus_stubs.a
}

#do_stage() {
#	ocamlfind install dbus META dBus.cmxa dBus.cma dBus.cmi dlldbus_stubs.so dBus.a libdbus_stubs.a
#}
