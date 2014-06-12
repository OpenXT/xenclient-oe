inherit xenclient-deb-utils

#do_NAME[noexec] = "1"

debinst_init() {
        [[ -z "${1}" ]] && die "deb_install: no path"
        cat <<EOFD >> "${1}"
#!/bin/bash

                unset CFLAGS
                unset CC
                unset LDFLAGS

                die() {
                        echo "ERROR: \$1"
                        exit 1
                }

                in_list() {
                        for ii in \${1}; do
                                [[ "\${ii}" == "\${2}" ]] && return 0
                        done
                        return 1
                }

                deb_suite="\${1}"
                deb_arch="\${2}"
                deb_no="\${3}"
                arg4="\${4}"
                is_lib=""
                is_lib_dev=""
		is_deb_src=""
                [[ -n "\${arg4}" && "\${arg4}" == "0" ]] && is_lib="1"
                [[ -n "\${arg4}" && "\${arg4}" == "1" ]] && is_lib_dev="1"
                [[ -n "\${arg4}" && "\${arg4}" == "2" ]] && is_deb_src="1"

                debian_dir="debian"
                [[ -z "\${is_lib_dev}" ]] || debian_dir="debian-dev"
		[[ -z "\${is_deb_src}" ]] || debian_dir="debian-src"

        version0=`echo "${PR}" | sed -re 's/r([0-9]+)-xc.*/\1/g'`
	( echo $version0 | egrep -q '^[0-9]+$' ) || version0="1.0"
                version="\${version0}-\${deb_no}"

		[[ -z "\${is_deb_src}" ]] || deb_arch="all"

                pkg_fname="${DEB_NAME}-\${version}_\${deb_arch}.deb"
                [[ -z "\${is_lib_dev}" ]] || pkg_fname="${DEB_NAME}-dev-\${version}_\${deb_arch}.deb"
		[[ -z "\${is_deb_src}" ]] || pkg_fname="${DEB_NAME}-src-\${version}_\${deb_arch}.deb"



        cd "${S}"
        mkdir -p "${D}"
        mkdir -p "${D}/\${debian_dir}/DEBIAN"
                pkg_name="${DEB_NAME}"
                [[ -z "\${is_lib_dev}" ]] || pkg_name="${DEB_NAME}-dev"
                [[ -z "\${is_deb_src}" ]] || pkg_name="${DEB_NAME}-src"

                (
                        set +e
                        ${APT_LOCK} dpkg -r \${pkg_name}
                        exit 0
                )

EOFD
}

debinst_prep() {
        [[ -z "${1}" ]] && die "deb_install: no path"
        cat <<EOFD >> "${1}"

		if [[ -n "\${is_deb_src}" ]]; then
				mkdir -p "${D}/\${debian_dir}/usr/src/\${pkg_name}-\${version}"
				D_SRC_DIR="${D}/oe-for-src"
				[[ -d "\${D_SRC_DIR}" ]] || D_SRC_DIR="${WORKDIR}/oe-for-src"
				( cd "\${D_SRC_DIR}" && find . -type d -name ".git" -exec rm -rf '{}' ';' )
				( cd "\${D_SRC_DIR}" && rsync -a . "${D}/\${debian_dir}/usr/src/\${pkg_name}-\${version}/" )
		else
			if [[ -d "${D}/oe-for-install" ]]; then
				( cd "${D}/oe-for-install" && rsync -a --exclude debian --exclude debian-dev --exclude oe-for-src . "${D}/\${debian_dir}"  )
			else
				( cd "${D}" && rsync -a --exclude debian --exclude debian-dev --exclude oe-for-staging --exclude oe-for-install --exclude oe-for-src . "${D}/\${debian_dir}"  )
			fi
		fi

                if [[ -z "\${is_lib_dev}" && -z "\${is_deb_src}" ]]; then
                        ## check whether libraries directly in /usr/lib/ Try to correct the package name if necessary
                        re_pkg_name=\$(echo "\${pkg_name}" | sed -re 's#[-\+\*\(\)\.\|]#\\\\&#g')


                        found_name=\$(
                                find "${D}/debian/lib" "${D}/debian/usr/lib"  "${D}/debian/lib64" "${D}/debian/usr/lib64" -maxdepth 1  -type f -o -type l  -name 'lib*.so*' 2>/dev/null | \
                                while read lpath; do
                                        [[ -z "\${lpath}" ]] && continue
                                        lib=\$(basename "\${lpath}")
                                        ( echo "\${lib}" | egrep -q "^\${re_pkg_name}[^a-zA-Z]*\.so\.[0-9]+\$" ) || continue
                                        objdump -p \${lpath} | sed -n -e's/^[[:space:]]*SONAME[[:space:]]*//p' | sed -e's/\([0-9]\)\.so\./\1-/; s/\.so\.//'
                                        break
                                done
                        )

                        if ! [[ -z  "\${found_name}" ]]; then 
                                [[ "\${pkg_name} != \${found_name}" ]] && \
                                die "DEB_NAME '\${pkg_name}' would not make lintian happpy. Suggested package name: '\${found_name}'"
                        fi
                fi

                ## eliminate unnecessary files for lib/lib-dev packages
                pushd "${D}/\${debian_dir}"
                        if ! [[ -z "\${is_lib}" ]]; then
                                [[ -d "usr/include/" ]] && rm -rf "usr/include" ]]
                                find lib/ usr/lib lib64 usr/lib64 \( -name 'lib*.so' -o -name 'lib*.la' -o -name 'lib*.a' \) \
                                        -exec rm -f '{}' ';'
                                [[ -d "usr/lib/pkgconfig/" ]] && rm -rf "usr/lib/pkgconfig"
                                [[ -d "pkgconfig/" ]] && rm -rf pkgconfig
                        elif ! [[ -z "\${is_lib_dev}" ]]; then
                                find lib/ usr/lib lib64 usr/lib64  -name 'lib*.so.*' -exec rm -f '{}' ';'
                                [[ -d "bin/" ]] && rm -rf bin
                                [[ -d "usr/bin/" ]] && rm -rf "usr/bin" ]]
                                [[ -d "sbin/" ]] && rm -rf sbin ]]
                                [[ -d "usr/sbin/" ]] && rm -rf "usr/sbin" ]]
                                [[ -d "usr/share/man/" ]] && rm -rf "usr/share/man"
                        fi
                popd


			pushd "${D}/\${debian_dir}"
			if [[ -z "\${is_deb_src}" ]]; then
				## strip executables, libs and create man pages
				texe=\$(mktemp -d)
				find "usr/bin/" "bin/" "sbin/" "usr/sbin/" -type f -executable 2> /dev/null | \
				(while read pexe; do
					exe=\$(basename "\${pexe}")
					strip "\${pexe}" 2> /dev/null

					in_list "${DEB_SKIP_MANPAGES}" "\${exe}" && continue

					[[ -e "usr/share/man/man1/\${exe}.1" ]] && continue

					## this workaround is actually needed as the --help produces some unwanted warning lines at the output
					cat <<EOF2 > "\${texe}/\${exe}"
#!/bin/bash
        export LD_LIBRARY_PATH="${D}/\${debian_dir}/usr/lib/:${D}/\${debian_dir}/usr/lib64/"
        if [[ -z "${DEB_MAN_PAGE_NO_MMHELP}" ]]; then
                exec -a "\${exe}" "\${pexe}" --help 2>&1 | sed -re 's#^.*invalid\s*option.*\\\$##gi'
        else
                exec -a "\${exe}" "\${pexe}"  2>&1
        fi
EOF2
					chmod 755 "\${texe}/\${exe}"
					[[ -d "usr/share/man/man1/" ]] || mkdir -p  "usr/share/man/man1/"
					if [[ -z \$(find "usr/share/man/" -type f -name "\${exe}." 2> /dev/null) ]]; then
						help2man  -n "Usage of \${exe}" -N -o "usr/share/man/man1/\${exe}.1" "\${texe}/\${exe}" 
						gzip -9 "usr/share/man/man1/\${exe}.1"
					fi
				done)

				( set +e; find "usr/share/man/" -type f -name '*.1' -exec gzip -9 '{}' ';' ; exit 0 )

				! [[ -z "\${texe}" ]] && [[ -e "\${texe}" ]] && rm -rf "\${texe}"
				find "lib" "usr/lib" "lib64" "usr/lib64" -type f -name 'lib*.so*' 2>/dev/null \ 
					-exec strip '{}' ';' \
					-exec chmod 644 '{}' ';'
				find "lib" "usr/lib" "lib64" "usr/lib64" -type f -name 'lib*.la*' -exec chmod 644 '{}' ';' 2> /dev/null
				#Emptying the dependency_libs field in .la files: 
				find "lib" "usr/lib" "lib64" "usr/lib64" -type f -name 'lib*.la*' -exec \
					sed -i "/dependency_libs/ s/'.*'/''/" '{}' ';' 2> /dev/null

				## determine library dependency info
				deps=\$( exec 2> /dev/null;
				find "usr/bin/" "bin/" "sbin/" "usr/sbin/" \
					"lib/"  "usr/lib"   "lib64" "usr/lib64" \( -type f -executable -o \( -name '*.so' -o -name '*.so.*' \) \) | \
					while read pobj; do
						ldd "\${pobj}" |  sed -n -re 's#\s*[^ \t]+\s*=>\s*(/[^ \t]+).*\$#\1#p' | xargs dpkg -S | sed -n -re 's#^\s*([^ \t:]+):.*\$#\1#p' 
					done | sort -u | sed -re "s/^\s*\${re_pkg_name}\s*\\\$//g" | tr '\n' ' ' | sed -re 's/^\s+//g' | sed -re 's/\s+\$//g' | sed -re 's/( +)/, /g'
				)

				## create shlibs file for the libs
				tmp_shlib="\$(mktemp)"
				find "lib" "usr/lib" "lib64" "usr/lib64" -type f -name 'lib*.so*' | \
					while read libpath; do
						libsoname=\$(objdump -p "\${libpath}" | grep SONAME | sed -n -re 's/\s*SONAME\s*(.+)$/\1/p' | sed -re 's/[ \t]+//g')
						libname=""
						libversion=""
						libname=\$(echo "\${libsoname}" | sed -n -re 's/^(.*)\-([0-9\.]+)\.so\$/\1/p')
						if ! [[ -z "\${libname}" ]]; then
							libversion=\$(echo "\${libsoname}" | sed -n -re 's/^(.*)\-([0-9\.]+)\.so\$/\2/p')
						else
							libname=\$(echo "\${libsoname}" | sed -n -re 's/^(.+)\.so\.(.+)\$/\1/p')
							libversion=\$(echo "\${libsoname}" | sed -n -re 's/^(.+)\.so\.(.+)\$/\2/p')
						fi
						[[ -z "\${libname}" || -z "\${libversion}" ]] && continue
						echo "\${libname} \${libversion} \${pkg_name}"
					done | sort -u > "DEBIAN/shlibs"
				[[ -e "DEBIAN/shlibs" && -z \$(cat "DEBIAN/shlibs") ]] && rm -f "DEBIAN/shlibs"
				[[ -e "DEBIAN/shlibs" ]] && \
				(
						cat <<\EOFSH > "DEBIAN/postinst"
#!/bin/sh
	set -e
	if [ "\$1" = "configure" ]; then
		ldconfig
	fi
EOFSH
						cat <<\EOFSH > "DEBIAN/postrm"
#!/bin/sh
	set -e
	if [ "\$1" = "remove" ]; then
		ldconfig
	fi
EOFSH
					chmod 755  "DEBIAN/postinst" "DEBIAN/postrm"
				)
			fi

                mkdir -p "usr/share/doc/\${pkg_name}"
                
                        ## copyright
                        cat - <<DEB_STEP > "usr/share/doc/\${pkg_name}/copyright"
Format-Specification: http://svn.debian.org/wsvn/dep/web/deps/dep5.mdwn?op=file&rev=135
Name: \${pkg_name}
Maintainer: ${DEB_PKG_MAINTAINER}

Files: *
Copyright: 2011 Citrix Systems.  All rights reserved.

License: GPL-2+
 This file is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 .
 This program is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.
 .
 On Debian systems, the complete text of the GNU General Public License
 (GPL) version 2 can be found an /usr/share/common-licenses/GPL-2".
 .
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.


DEB_STEP

                        if [[ -n "${DEB_EXTRA_DEPS}" ]]; then
                                [[ -n "\${deps}" ]] && deps+=", "
                                deps+="${DEB_EXTRA_DEPS}"
                        fi

                        ldeb_section="${DEB_SECTION}"
                        [[ -z "\${is_lib_dev}" ]] || ldeb_section="libdevel"

                        ## control
                        deb_depends=""
                        if ! [[ -z "\${deps}" ]]; then
                                deb_depends+=\$'\n'
                                deb_depends+="Depends: \${deps}"
                        fi
                        cat - <<DEB_STEP > "DEBIAN/control"
Package: \${pkg_name}
Section: \${ldeb_section}
Source: \${pkg_name}
Version: \${version}
Architecture: \${deb_arch}
Maintainer: ${DEB_PKG_MAINTAINER}\${deb_depends}
Priority: optional
Homepage: http://www.citrix.com
Description: ${DEB_DESC}
 This is part of XenClient Tools for linux guests. ${DEB_DESC_EXT}
DEB_STEP


                        ## some lintian overrides
                        mkdir -p "usr/share/lintian/overrides/"
                        cat - > "usr/share/lintian/overrides/\${pkg_name}" <<EOFOVD
\${pkg_name}: extended-description-is-empty
\${pkg_name}: changelog-file-missing-in-native-package
\${pkg_name}: non-dev-pkg-with-shlib-symlink
\${pkg_name}: debian-changelog-file-missing
EOFOVD
        
        popd

EOFD
}

debinst_mkpkg() {
        [[ -z "${1}" ]] && die "deb_install: no path"
        cat <<EOFD >> "${1}"

                [[ -d "${D}/\${debian_dir}/" ]] || die "debinst_mkpkg(): the DEBIAN_DIR not found"

                pushd "${D}/\${debian_dir}"
                        ## for some reasons the pkgconfig ends up where it should not be
                        if [[ -d  "pkgconfig" ]]; then
                                mkdir -p "usr/lib/"
                                mv "pkgconfig" "usr/lib/"
                        fi

                        ## md5sums
                        find * -name 'DEBIAN' -prune -o -type f -exec md5sum '{}' ';' > DEBIAN/md5sums
                popd


                ## create package
                (
                        set -e
                        flock -x 220
                chown -R 0:0 "${D}"
                cd "${D}" 
                        dpkg-deb --build "\${debian_dir}" 
                        mv "${D}/\${debian_dir}.deb" "\${pkg_fname}" 
                )  220> ${CHOWN_LCK_FILE}

                ## debian validate package 
                exit_status=\$?
                if [[ -z "${DEB_SKIP_LINTIAN}" && -z "\${is_deb_src}" ]]; then
                        ( set -e; cd "${D}" && lintian --allow-root "\${pkg_fname}" )
                        exit_status=\$?
                fi

                if [[ -z "${IS_DKMS}" ]]; then
                        ##install package for calculating deps if necessary
                        (
                                set +e
                                cd "${D}"
                                ${APT_LOCK} dpkg -i "\${pkg_fname}"
                                exit 0
                        )
                fi


                exit \${exit_status}
EOFD
}


install_extra_pkg() {
    local chroot="$1"
        local arch="$2"
    local pkgs="$3"

        ## no need of installing packages anymore. The chroot should contain all necessary ones
        return 0

        (
                set +e
        my_chroot "${chroot}" "${arch}" ${APT_LOCK} apt-get install -y -f ${DEB_EXTA_PKG_TOOLS}
        my_chroot "${chroot}" "${arch}" ${APT_LOCK} apt-get install -y -f ${pkgs}
                exit 0;
        )
}

my_step() {
    local step="$1"
        local deb_no=0
        local install_dev=""
	local install_src=""

	[[ -z "${DEB_CREATE_SRC}" ]] || install_src="1"

    chmod +x "${S}/oe_do_${step}.sh"
    for suite in ${DEB_SUITE}
    do
        for arch in ${DEB_ARCH}
        do
                        (( deb_no = ${deb_no} + 1 ))
            build="${CHROOT_PATH}/${suite}_${arch}"
            prepare_chroot "${build}" "${arch}"
            sudo mkdir -p "${build}/${S}"
                    
            case "$step" in
                configure)
                    update_chroot "${build}" "${arch}"
                    install_extra_pkg "${build}" "${arch}" "${DEB_EXTRA_PKGS}"
		    if [[ -n "${install_src}" ]]; then
			sudo mkdir -p "${build}/${WORKDIR}/oe-for-src"
			sudo rsync -ar "${S}/" "${build}/${WORKDIR}/oe-for-src"
			sudo chown -R "${BUILD_UID}" "${build}/${WORKDIR}/oe-for-src"
	            fi
		    [[ -n "${D}" && -d "${build}${D}" ]] && sudo rm -rf --one-file-system "${build}${D}"
                    ;;
                 install)
                                        ## check some important debian variables are set in the recipe
                                        [[ -z "${DEB_NAME}" ]] && die "DEB_NAME variable is not set in the recipe"
                                        [[ -z "${DEB_DESC}" ]] && die "DEB_DESC variable is not set in the recipe"
                                        [[ -z "${DEB_SECTION}" ]] && die "DEB_SECTION variable is not set in the recipe"
                                        [[ -z "${DEB_CREATEDEV}" ]] || install_dev="1"

                    [[ -d "${D}/packages/${suite}/${arch}" ]] || sudo mkdir -p "${D}/packages/${suite}/${arch}" 
					( set +e; sudo chown -R "${BUILD_UID}" "${D}"; exit 0 )

                       ;;
            esac

            sudo rsync -azr "${S}/" "${build}/${S}"
            sudo chown -R "${BUILD_UID}" "${build}/${S}" 
            if ! [[ -z "${install_dev}" ]]; then
                ## needed twice to create package and package-dev .deb packages 
                my_chroot "${build}" "${arch}" "${S}/oe_do_${step}.sh" "${suite}" "${arch}" "${deb_no}" "0"
                ( set +e; sudo cp "${build}/${D}"/*.deb "${D}/packages/${suite}/${arch}"; exit 0; )
                my_chroot "${build}" "${arch}" "${S}/oe_do_${step}.sh" "${suite}" "${arch}" "${deb_no}" "1"
            else 
                my_chroot "${build}" "${arch}" "${S}/oe_do_${step}.sh" "${suite}" "${arch}" "${deb_no}"
            fi

	    if [[ -n "${install_src}" ]]; then
                my_chroot "${build}" "${arch}" "${S}/oe_do_${step}.sh" "${suite}" "${arch}" "${deb_no}" "2"
                ( set +e; sudo cp "${build}/${D}"/*.deb "${D}/packages/${suite}/${arch}"; exit 0; )
	    fi

           [[ -d "${D}/packages" ]] && sudo chown -R "${BUILD_UID}" "${D}/packages" 
            case "$step" in
                install)
                    [[ -d "${D}/packages/${suite}/${arch}" ]] || sudo mkdir -p "${D}/packages/${suite}/${arch}"
                     sudo cp "${build}/${D}"/*.deb "${D}/packages/${suite}/${arch}"
                     sudo chown -R "${BUILD_UID}" "${D}" 
		     sudo mkdir -p "${DEB_REPO_DIR}/packages/${suite}"
		     sudo chown -R "${BUILD_UID}" "${DEB_REPO_DIR}/packages"
		     cp -a "${D}/packages/${suite}/${arch}"/*.deb "${DEB_REPO_DIR}/packages/${suite}/"
                        ;;
            esac
            ( set +e; sudo chown -R "${BUILD_UID}" "${D}" ; exit 0 )
        done
    done
}





do_compile_append() {
EOF
    my_step "compile"
}


#do_stage_append() {
#EOF
#    my_step "stage"
#}

do_configure_prepend() {
cat <<EOF > "${S}/oe_do_configure.sh"
#!/bin/bash
#set -x
                unset CFLAGS
                unset CC
                unset LDFLAGS
        cd "${S}"
}

do_configure() {
   autoreconf -i
   ./configure --prefix=/usr
}

do_configure_append() {
EOF
    my_step "configure"
}

do_compile() {
    make
}


#do_my_stage() {
#        ## this happens in chroot
#        if [[ -d "${D}/oe-for-staging" ]]; then
#                ( cd "${D}/oe-for-staging/" && rsync -a . / )
#        elif [[ -d "${D}/oe-for-install" ]]; then
#                ( cd "${D}/oe-for-install/" && rsync -a . / )
#        else
#                ( cd "${D}" && rsync -a --exclude DEBIAN . / )
#        fi
#        ( set +e; ldconfig; exit 0 )
#}


do_compile_prepend() {
cat <<EOF > "${S}/oe_do_compile.sh"
#!/bin/bash
                set -e
                unset CFLAGS
                unset CC
                unset LDFLAGS
                unset MACHTYPE
                unset HOSTTYPE
                set 

        cd "${S}"
}

do_install_prepend() {
        local oe_step="${S}/oe_do_install.sh"
        debinst_init            "${oe_step}"

        cat <<EOF >> "${oe_step}"
        ## do_install part here ... (in chroot)
}
do_install() {
   make install DESTDIR="${D}"
}
do_install_append() {
        if [[ -d "${D}/oe-for-staging" ]]; then
                ( cd "${D}/oe-for-staging/" && rsync -a . / )
        elif [[ -d "${D}/oe-for-install" ]]; then
                ( cd "${D}/oe-for-install/" && rsync -a . / )
        else
                ( cd "${D}" && rsync -a --exclude DEBIAN . / )
        fi
        if [[ -e "${S}/DEBIAN_preinst" ]]; then
                ( mkdir -p "${D}/DEBIAN" && cp "${S}/DEBIAN_preinst" "${D}/DEBIAN/preinst" )
        fi
        if [[ -e "${S}/DEBIAN_postinst" ]]; then
                ( mkdir -p "${D}/DEBIAN" && cp "${S}/DEBIAN_postinst" "${D}/DEBIAN/postinst" )
        fi
        if [[ -e "${S}/DEBIAN_prerm" ]]; then
                ( mkdir -p "${D}/DEBIAN" && cp "${S}/DEBIAN_prerm" "${D}/DEBIAN/prerm" )
        fi
        if [[ -e "${S}/DEBIAN_postrm" ]]; then
                ( mkdir -p "${D}/DEBIAN" && cp "${S}/DEBIAN_postrm" "${D}/DEBIAN/postrm" )
        fi
        ( set +e; ldconfig; exit 0 )
EOF
        local oe_step="${S}/oe_do_install.sh"
        debinst_prep "${oe_step}"
        debinst_mkpkg   "${oe_step}"
    my_step "install"
}


#do_stage_prepend() {
#cat <<EOF > "${S}/oe_do_stage.sh"
##!/bin/bash
#        cd "${S}"
#}

