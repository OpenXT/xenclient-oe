EXTRA_CABAL_CONF=""

do_configure() {
	${RUNSETUP} clean

	#CC="${CCACHE}${BUILD_PREFIX}gcc" \
	CFLAGS="${BUILD_CFLAGS}" \
	CPP=`which cpp` \
	${RUNSETUP} configure ${EXTRA_CABAL_CONF} --package-db=${LOCAL_GHC_PACKAGE_DATABASE} --ghc-options='-pgml ./ghc-ld' --with-hsc2hs-ld="${S}/ghc-ld"  --enable-shared --with-compiler=ghc-${GHC_VERSION} --prefix=${prefix} --extra-include-dirs="${STAGING_INCDIR}" --extra-lib-dirs="${STAGING_LIBDIR}" --libsubdir=ghc-local/\$pkgid --with-gcc=./ghc-cc
}
