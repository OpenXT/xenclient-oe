EXTRA_CABAL_CONF=""

do_configure() {
	${RUNSETUP} clean

	#CC="${CCACHE}${BUILD_PREFIX}gcc" \
        # Below was BUILD_CFLAGS but we should let OE set the proper value by using CFLAGS
        # Also changed CPP from `which cpp` as that can also be decided by OE
        # The previous instance of this in devshell was also changed
        CFLAGS="${CFLAGS}" \
        CPP="${CPP}" \
	${RUNSETUP} configure ${EXTRA_CABAL_CONF} --package-db=${LOCAL_GHC_PACKAGE_DATABASE} --ghc-options='-pgml ./ghc-ld' --with-hsc2hs-ld="${S}/ghc-ld"  --enable-shared --with-compiler=ghc-${GHC_VERSION} --prefix=${prefix} --extra-include-dirs="${STAGING_INCDIR}" --extra-lib-dirs="${STAGING_LIBDIR}" --libsubdir=ghc-local/\$pkgid --with-gcc=./ghc-cc
}
