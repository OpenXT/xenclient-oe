SUMMARY = "Haskell (GHC) runtime"
DESCRIPTION = "Haskell is an advanced purely-functional programming language. An open-source \
               product of more than twenty years of cutting-edge research, it allows rapid \ 
               development of robust, concise, correct software. With strong support for \
               integration with other languages, built-in concurrency and parallelism, \
               debuggers, profilers, rich libraries and an active community, Haskell makes it \
               easier to produce flexible, maintainable, high-quality software."
AUTHOR = "Adam Oliver <aikidokatech@users.noreply.github.com>"
HOMEPAGE = "http://www.haskell.org/"
SECTION = "devel"
LICENSE = "Glascow"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7cb08deb79c4385547f57d6bb2864e0f"

FILESEXTRAPATHS_prepend := "${THISDIR}/ghc-${PV}:"

SRC_URI = " \
	http://www.haskell.org/ghc/dist/6.12.1/ghc-6.12.1-src.tar.bz2 \
	file://bfd-error.patch \
	file://ghc6-fix-linking-with-newer-binutils.patch \
	file://fix-linker.patch \
	file://fix-compile-with-gcc-4.9.patch \
	"

SRC_URI[md5sum] = "3a2b23f29013605f721ebdfc29de9c92"
SRC_URI[sha256sum] = "cdf99f9add677a925ee87a5b87e94eb595ed9b72034453c195ef9379bd26552a"

DEPENDS = "pkgconfig-native"

S = "${WORKDIR}/ghc-${PV}"

BBCLASSEXTEND = "native"

#EXTRA_OECONF_append = " --bindir ${STAGING_BINDIR} --libdir ${STAGING_LIBDIR} --datadir ${STAGING_DATADIR} --enable-shared "

inherit autotools-brokensep pkgconfig

FILES_${PN} = "${libdir}/*.so"
FILES_${PN}-dev = ""

PARALLEL_MAKE=""

# Inheriting autotools now so probalby a better way to do this but could not get it to work
do_configure() {
	./configure --prefix=${STAGING_DIR} --bindir ${STAGING_BINDIR} --libdir ${STAGING_LIBDIR} --datadir ${STAGING_DATADIR} --enable-shared
}

do_pre_configure() {
	cp `which pwd` ${S}/utils/ghc-pwd/ghc-pwd
	export CPP=`which cpp`
}

addtask pre_configure after do_patch before do_configure

do_tweak_configuration() {
	# This goes here so we do not depend nor require libgmp installed on target
	echo "INTEGER_LIBRARY = integer-simple" > mk/build.mk
	# Various flags to trim down ghc build time
	echo "HADDOCK_DOCS = NO" >> mk/build.mk
	echo "BUILD_DOCBOOK_HTML = NO" >> mk/build.mk
	echo "BUILD_DOCBOOK_PS = NO" >> mk/build.mk
	echo "BUILD_DOCBOOK_PDF = NO" >> mk/build.mk
	echo "SplitObjs = NO" >> mk/build.mk
	echo "SRC_HC_OPTS = -H64m -O0 -fasm" >> mk/build.mk
	echo "GhcStage1HcOpts = -O -fasm" >> mk/build.mk
	echo "GhcStage2HcOpts = -O0 -fasm" >> mk/build.mk
	echo "GhcLibHcOpts    = -O -fasm" >> mk/build.mk
	# set this to "v p" if profiling libraries are necessary
	echo "GhcLibWays = v dyn" >> mk/build.mk

	# look there for bfd.h stupid cow:
	echo "STANDARD_OPTS += \"-I${STAGING_INCDIR_NATIVE}\"" >> rts/ghc.mk
}

addtask tweak_configuration after do_configure before do_compile

#do_install() {
#    make install "DESTDIR=${D}"
#}

do_prep_runtime_files() {
    install -d ${D}/usr/lib
    install -m 755 ${S}/rts/dist/build/libHSrts-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/rts/dist/build/libHSrts_thr-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libffi/dist-install/build/libHSffi-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/array/dist-install/build/libHSarray-0.3.0.0-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/base/dist-install/build/libHSbase-4.2.0.0-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/containers/dist-install/build/libHScontainers-0.3.0.0-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/time/dist-install/build/libHStime-1.1.4-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/pretty/dist-install/build/libHSpretty-1.0.1.1-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/directory/dist-install/build/libHSdirectory-1.0.1.0-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/old-locale/dist-install/build/libHSold-locale-1.0.0.2-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/syb/dist-install/build/libHSsyb-0.1.0.2-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/process/dist-install/build/libHSprocess-1.0.1.2-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/random/dist-install/build/libHSrandom-1.0.0.2-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/filepath/dist-install/build/libHSfilepath-1.1.0.3-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/bytestring/dist-install/build/libHSbytestring-0.9.1.5-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/integer-simple/dist-install/build/libHSinteger-simple-0.1.0.0-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/old-time/dist-install/build/libHSold-time-1.0.0.3-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/utf8-string/dist-install/build/libHSutf8-string-0.3.4-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/unix/dist-install/build/libHSunix-2.4.0.0-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/ghc-prim/dist-install/build/libHSghc-prim-0.2.0.0-ghc6.12.1.so ${D}/usr/lib
    install -m 755 ${S}/libraries/haskell98/dist-install/build/libHShaskell98-1.0.1.1-ghc6.12.1.so ${D}/usr/lib
}

addtask prep_runtime_files after do_install before do_package

# RPROVIDES is coming up empty for this package.  Workaround until cause is discovered.
RPROVIDES_${PN} = "ghc-runtime"

