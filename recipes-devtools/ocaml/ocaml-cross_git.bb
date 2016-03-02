SECTION = "devel"
LICENSE = "QPL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=524443efef4a3e092cca058d99996c88"

PR = "r2"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/ocaml.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH} \
           file://0007-Fix-ocamlopt-w.r.t.-binutils-2.21.patch;patch=1 \
	   file://config.patch \
           file://remove-absolute-linker-path-from-lib.patch \
"

inherit xenclient
#inherit native
inherit cross

FILESEXTRAPATHS_prepend := "${THISDIR}/ocaml-cross:"

S = "${WORKDIR}/git"

RDEPENDS_${PN}-dev = ""

do_configure() {
#	linux32 ./configure -no-curses -prefix ${STAGING_DIR} \
#		-bindir ${STAGING_BINDIR} -libdir ${STAGING_LIBDIR} \
#	        -no-tk -cc "$CC -m32" -as "$AS --32" -aspp "$CC -m32 -c"
#	Ugly fix to cross-compile. I think we need to use cross-compil patch
#	for ocaml
	CFLAGS="${BUILD_CFLAGS} -m32" \
		linux32 ./configure -no-curses \
	        	-bindir ${bindir} \
			-libdir ${libdir}/ocaml \
			-mandir ${datadir}/man \
            -cc "${TARGET_PREFIX}gcc -m32 --sysroot=${STAGING_DIR_TARGET}" -mksharedlib "${TARGET_PREFIX}ld -shared" \
			-no-tk -as "${TARGET_PREFIX}as --32" -aspp "${TARGET_PREFIX}gcc -m32 -c"

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

#do_stage() {
#	make install
#	mv -v ${STAGING_LIBDIR}/ocaml/caml ${STAGING_INCDIR}/
#}

do_install() {
	make PREFIX="${D}/${prefix}" BINDIR="${D}/${bindir}" LIBDIR="${D}/${libdir}/ocaml" MANDIR="${D}/${datadir}/man" install
}

do_package[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_rpm[noexec] = "1"
do_package_write_deb[noexec] = "1"

