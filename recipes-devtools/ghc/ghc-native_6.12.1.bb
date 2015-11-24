# Our own GHC recipe as it was not provided in another layer.  GHC uses autotools,
# so redoing to be styled better and written properly for passing options.
# - Adam Oliver
# TODO:  This is an individual native recipe.  Should be changed to and extend using
# BBCLASSEXTEND in a GHC recipe.

SUMMARY = "ghc-native"
DESCRIPTION = "GHC"
LICENSE = "Glasgow"

LIC_FILES_CHKSUM = "file://LICENSE;md5=7cb08deb79c4385547f57d6bb2864e0f"

FILESEXTRAPATHS_prepend := "${THISDIR}/ghc-${PN}:"

SRC_URI = " \
	http://www.haskell.org/ghc/dist/6.12.1/ghc-6.12.1-src.tar.bz2 \
	file://bfd-error.patch \
	file://ghc6-fix-linking-with-newer-binutils.patch \
	file://fix-linker.patch \
	file://fix-compile-with-gcc-4.9.patch \
	"

SRC_URI[md5sum] = "3a2b23f29013605f721ebdfc29de9c92"
SRC_URI[sha256sum] = "cdf99f9add677a925ee87a5b87e94eb595ed9b72034453c195ef9379bd26552a"

#S = "${WORKDIR}/${PN}-${PV}"

PR = "r1"

S = "${WORKDIR}/ghc-${PV}"

inherit native pythonnative

PARALLEL_MAKE=""
CFLAGS_append = ' -Wno-unused -std=gnu89'

# huge hack to be able to generate ipks from native package
#python() {
#    bb.data.delVarFlag("do_package", "noexec", d);
#    bb.data.delVarFlag("do_package_write_ipk", "noexec", d);
#    d.setVarFlag('do_configure', 'umask', 022)
#    d.setVarFlag('do_compile', 'umask', 022)
#    d.appendVarFlag('do_install', 'depends', ' virtual/fakeroot-native:do_populate_sysroot')
#    d.setVarFlag('do_install', 'fakeroot', 1)
#    d.setVarFlag('do_install', 'umask', 022)
#    d.appendVarFlag('do_package', 'depends', ' virtual/fakeroot-native:do_populate_sysroot')
#    d.setVarFlag('do_package', 'fakeroot', 1)
#    d.setVarFlag('do_package', 'umask', 022)
#    d.setVarFlag('do_package_setscene', 'fakeroot', 1)
#}

# Inheriting autotools now so probalby a better way to do this
do_configure() {
	cp `which pwd` utils/ghc-pwd/ghc-pwd
	export CPP=`which cpp`
	./configure --prefix=${STAGING_DIR} --bindir ${STAGING_BINDIR} --libdir ${STAGING_LIBDIR} --datadir ${STAGING_DATADIR} --enable-shared

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

## This is probably preventing the use of CFLAGS_append as it is not oe_make
do_compile() {
	oe_runmake
}

do_install() {
    oe_runmake install "DESTDIR=${D}"
#     this is for runtime package
#    install -d ${D}/usr/lib
#    install -m 755 ${S}/rts/dist/build/libHSrts-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/rts/dist/build/libHSrts_thr-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libffi/dist-install/build/libHSffi-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/array/dist-install/build/libHSarray-0.3.0.0-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/base/dist-install/build/libHSbase-4.2.0.0-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/containers/dist-install/build/libHScontainers-0.3.0.0-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/time/dist-install/build/libHStime-1.1.4-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/pretty/dist-install/build/libHSpretty-1.0.1.1-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/directory/dist-install/build/libHSdirectory-1.0.1.0-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/old-locale/dist-install/build/libHSold-locale-1.0.0.2-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/syb/dist-install/build/libHSsyb-0.1.0.2-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/process/dist-install/build/libHSprocess-1.0.1.2-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/random/dist-install/build/libHSrandom-1.0.0.2-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/filepath/dist-install/build/libHSfilepath-1.1.0.3-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/bytestring/dist-install/build/libHSbytestring-0.9.1.5-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/integer-simple/dist-install/build/libHSinteger-simple-0.1.0.0-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/old-time/dist-install/build/libHSold-time-1.0.0.3-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/utf8-string/dist-install/build/libHSutf8-string-0.3.4-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/unix/dist-install/build/libHSunix-2.4.0.0-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/ghc-prim/dist-install/build/libHSghc-prim-0.2.0.0-ghc6.12.1.so ${D}/usr/lib
#    install -m 755 ${S}/libraries/haskell98/dist-install/build/libHShaskell98-1.0.1.1-ghc6.12.1.so ${D}/usr/lib
}

#FILES_ghc-runtime-native += "/usr/lib/*.so"
#PACKAGES =+ "ghc-runtime-native"
#RDEPENDS_${PN} =+ "ghc-runtime-native"
#RPROVIDES_${PN} = "ghc-runtime-native"
