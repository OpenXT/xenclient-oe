DEPENDS  = "ghc-native"
RDEPENDS = "ghc-runtime"
GHC_VERSION = "6.12.1"
GHC_CACHE_FILE = "${STAGING_LIBDIR_NATIVE}/ghc-${GHC_VERSION}/package.conf.d/package.cache"
GHC_CACHE_FILE_BACKUP = "${STAGING_DIR}/package.cache.backup"
PSTAGE_FORCEDEPS += "${PN}"

EXCLUSIVE_CONFIGURE_LOCK=1

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

