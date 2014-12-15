ASNEEDED_pn-${PN} = ""
DEPENDS  = "ghc-native"
RDEPENDS = "ghc-runtime-native"
GHC_VERSION = "6.12.1"
GHC_CACHE_FILE = "${STAGING_LIBDIR_NATIVE}/ghc-${GHC_VERSION}/package.conf.d/package.cache"
GHC_CACHE_FILE_BACKUP = "${STAGING_DIR}/package.cache.backup"
PSTAGE_FORCEDEPS += "${PN}"

EXCLUSIVE_CONFIGURE_LOCK = "1"

RUNGHC     = "runghc"
GHCRECACHE = "ghc-pkg recache"
RUNSETUP  = "${RUNGHC} ${SETUPFILE}"
SETUPFILE = "$([ -f Setup.lhs ] && echo Setup.lhs || echo Setup.hs)"

LOCAL_GHC_PACKAGE_DATABASE = "${S}/local-packages.db"
export GHC_PACKAGE_PATH = "${LOCAL_GHC_PACKAGE_DATABASE}:"

# ghc libs have (and always had) broken rpath disable sanity checking for now
INSANE_SKIP_${PN} = "1"
INSANE_SKIP_${PN}-dev = "1"
INSANE_SKIP_${PN}-dbg = "1"


do_configure_prepend() {
    rm -rf "${LOCAL_GHC_PACKAGE_DATABASE}"
    if [ ! -e "${LOCAL_GHC_PACKAGE_DATABASE}" ];then
        ghc-pkg init "${LOCAL_GHC_PACKAGE_DATABASE}"
        for file in ${STAGING_LIBDIR}/ghc-local/*.conf
        do
            if [ -e "$file" ]; then
                    cat "$file"| sed -e "s|: */usr/lib|: ${STAGING_LIBDIR}|"| ghc-pkg --force -f "${LOCAL_GHC_PACKAGE_DATABASE}" update -
            fi
        done
    fi

	# create CC & LD scripts matching the env config
	cd ${S}
	cat << EOF > ghc-cc
#!/bin/sh
exec ${CC} ${CFLAGS} "\$@"
EOF
	chmod +x ghc-cc

	cat << EOF > ghc-ld
#!/bin/sh
exec ${CC} ${LDFLAGS} "\$@"
EOF
	chmod +x ghc-ld

	cat << EOF > ld
#!/bin/sh
exec ${LD} "\$@"
EOF
	chmod +x ld

}

ghc_pkg_prep_devshell() {
    rm -rf "${LOCAL_GHC_PACKAGE_DATABASE}"
    if [ ! -e "${LOCAL_GHC_PACKAGE_DATABASE}" ]; then
        ghc-pkg init "${LOCAL_GHC_PACKAGE_DATABASE}"
	echo "initing db ${PWD}/${LOCAL_GHC_PACKAGE_DATABASE}" >> ${S}/devshell_log
        for file in ${STAGING_LIBDIR}/ghc-local/*.conf
        do
            if [ -e "$file" ]; then
		    echo "adding $file" >> ${S}/devshell_log
                    cat "$file"| sed -e "s|: */usr/lib|: ${STAGING_LIBDIR}|"| ghc-pkg --force -f "${LOCAL_GHC_PACKAGE_DATABASE}" update -
            fi
        done
    fi

	# create CC & LD scripts matching the env config
	cd ${S}
	cat << EOF > ghc-cc
#!/bin/sh
exec ${CC} ${CFLAGS} "\$@"
EOF
	chmod +x ghc-cc

	cat << EOF > ghc-ld
#!/bin/sh
exec ${CC} ${LDFLAGS} "\$@"
EOF
	chmod +x ghc-ld

	cat << EOF > ld
#!/bin/sh
exec ${LD} "\$@"
EOF
	chmod +x ld

	cat << EOF > runconfigure
#!/bin/sh
        CFLAGS="${BUILD_CFLAGS}" \
        CPP=`which cpp` \
        ${RUNSETUP} configure --package-db=${LOCAL_GHC_PACKAGE_DATABASE} --enable-shared --with-compiler=ghc-${GHC_VERSION} --ghc-options='-pgmc ./ghc-cc -pgml ./ghc-ld' --with-gcc=./ghc-cc --prefix=${prefix} --libsubdir=ghc-local/\$pkgid ${CABAL_CONFIGURE_EXTRA_OPTS}
EOF

	cat << EOF > runbuild
#!/bin/sh
${RUNSETUP} --ghc-options='-pgmc ./ghc-cc -pgml ./ghc-ld' --with-gcc=./ghc-cc build
EOF
	chmod +x runconfigure runbuild
}

ghc_pkg_cleanup_devshell() {
	rm ghc-ld ghc-cc ld;
	rm runconfigure; runbuild
}

do_devshell[prefuncs] += "ghc_pkg_prep_devshell"
do_devshell[postfuncs] += "ghc_pkg_cleanup_devshell"

do_configure() {
        ${RUNSETUP} clean

        #CC="${CCACHE}${BUILD_PREFIX}gcc" \
        CFLAGS="${BUILD_CFLAGS}" \
        CPP=`which cpp` \
        ${RUNSETUP} configure --package-db=${LOCAL_GHC_PACKAGE_DATABASE} --enable-shared --with-compiler=ghc-${GHC_VERSION} --ghc-options='-pgmc ./ghc-cc -pgml ./ghc-ld' --with-gcc=./ghc-cc --prefix=${prefix} --libsubdir=ghc-local/\$pkgid ${CABAL_CONFIGURE_EXTRA_OPTS}
}

do_compile() {
        ${RUNSETUP} --ghc-options='-pgmc ./ghc-cc -pgml ./ghc-ld' --with-gcc=./ghc-cc build
}

