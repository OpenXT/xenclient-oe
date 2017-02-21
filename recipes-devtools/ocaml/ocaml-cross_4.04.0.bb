SRC_URI[md5sum] = "496f99822133bb905171bae8f64b7be4"
SRC_URI[sha256sum] = "cb63486f6cb837a5a57ab93e4c429551127561d0da61b7712116769a6a8322de"
SECTION = "devel"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4f72f33f302a53dc329f4d3819fe14f9"

PR = "r3"

SRC_URI = "http://caml.inria.fr/pub/distrib/ocaml-4.04/ocaml-4.04.0.tar.gz \
	   file://config.patch \
"

inherit xenclient
#inherit native
inherit cross

FILESEXTRAPATHS_prepend := "${THISDIR}/ocaml-cross:"

S = "${WORKDIR}/ocaml-${PV}"

RDEPENDS_${PN}-dev = ""

do_configure() {
#	Ugly fix to cross-compile. I think we need to use cross-compil patch
#	for ocaml
	CFLAGS="${BUILD_CFLAGS} -m32" \
		linux32 ./configure -no-curses \
	        	-bindir ${bindir} \
			-libdir ${libdir}/ocaml \
			-mandir ${datadir}/man \
            -cc "${TARGET_PREFIX}gcc -m32 --sysroot=${STAGING_DIR_TARGET}" -mksharedlib "${TARGET_PREFIX}ld -shared" \
			-as "${TARGET_PREFIX}as --32" -aspp "${TARGET_PREFIX}gcc -m32 -c"

	sed -i'' -re 's/-lX11//' config/Makefile
	sed -i'' -re 's/OTHERLIBRARIES=.*/OTHERLIBRARIES=unix str num dynlink bigarray systhreads threads/' config/Makefile
	sed -i'' -re 's/NATIVECCCOMPOPTS=(.*)/NATIVECCCOMPOPTS=\1 -fno-stack-protector/' config/Makefile
	sed -i'' -re 's/BYTECCCOMPOPTS=(.*)/BYTECCCOMPOPTS=\1 -fno-stack-protector/' config/Makefile
}

do_compile() {
	oe_runmake world
	mkdir -p ${WORKDIR}/targetcc
	ln -sf ${CROSS_DIR}/bin/${HOST_PREFIX}gcc ${WORKDIR}/targetcc/gcc
	make opt PATH="${WORKDIR}/targetcc:${PATH}"
}

do_install() {
	make PREFIX="${D}/${prefix}" BINDIR="${D}/${bindir}" LIBDIR="${D}/${libdir}/ocaml" MANDIR="${D}/${datadir}/man" install
}

do_package[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_rpm[noexec] = "1"
do_package_write_deb[noexec] = "1"

