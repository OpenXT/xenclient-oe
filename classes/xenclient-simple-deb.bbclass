inherit xenclient-deb-utils



do_simple_deb_package() {
    local deb_no=0
    for suite in ${DEB_SUITE}
    do
        for arch in ${DEB_ARCH}
        do
            local build="${CHROOT_PATH}/${suite}_${arch}"
            prepare_chroot "${build}" "${arch}"
            sudo mkdir -p "${build}/${S}"
            ( set +e; sudo mkdir -p "${build}/${WORKDIR}"; exit 0 )
	    ( cd "${WORKDIR}" && sudo cp -a . "${build}/${WORKDIR}" )
            sudo mkdir -p "${build}/${D}"
            update_chroot "${build}" "${arch}"
            ( cd "${D}" && sudo cp -a . "${build}/${D}/" )
            cat <<EOSF > "${S}/oe_do_install.sh"
#!/bin/bash
        set -e
	set -x
        cd "${S}"
        mkdir -p "${D}/debian_pkg/DEBIAN"
        ( set -e; cd "${D}" && ls -alR . > /tmp/1 && rsync -ar . --exclude "/packages" debian_pkg/ && ls -alR . > /tmp/2 && rm -rf debian_pkg/debian_pkg )
        pushd "${D}/debian_pkg/"

		if ! [[ -z "${DEB_DO_NOT_INCLUDE}" ]]; then
			for path in "" ${DEB_DO_NOT_INCLUDE}; do
				[[ -z "\${path}" ]] || rm -rf "./\${path}"
			done
		fi

        version=`echo "${PR}" | sed -re 's/r([0-9]+)-xc.*/\1/g'`
        ( echo $version | egrep -q '^[0-9]+$' ) || version="1.0"
        version="\${version}-${suite}"
        pkg_fname="${DEB_NAME}-\${version}_${arch}.deb"
        cat - <<DEB_STEP > "DEBIAN/control"
Package: ${DEB_NAME}
Section: ${DEB_SECTION}
Source: ${DEB_NAME}
Version: \${version}
Architecture: ${arch}
Maintainer: ${DEB_PKG_MAINTAINER}
Priority: optional
Homepage: http://www.citrix.com
Description: ${DEB_DESC}
 ${DEB_DESC_EXT}
DEB_STEP

        if [[ -e "${WORKDIR}/DEBIAN_preinst" ]]; then
                cp "${WORKDIR}/DEBIAN_preinst" "${D}/debian_pkg/DEBIAN/preinst"
		chmod 755 "${D}/debian_pkg/DEBIAN/preinst"
        fi
        if [[ -e "${WORKDIR}/DEBIAN_postinst" ]]; then
                cp "${WORKDIR}/DEBIAN_postinst" "${D}/debian_pkg/DEBIAN/postinst"
		chmod 755 "${D}/debian_pkg/DEBIAN/postinst"
        fi
        if [[ -e "${WORKDIR}/DEBIAN_prerm" ]]; then
                cp "${WORKDIR}/DEBIAN_prerm" "${D}/debian_pkg/DEBIAN/prerm"
		chmod 755 "${D}/debian_pkg/DEBIAN/prerm"
        fi
        if [[ -e "${WORKDIR}/DEBIAN_postrm" ]]; then
                cp "${WORKDIR}/DEBIAN_postrm" "${D}/debian_pkg/DEBIAN/postrm"
		chmod 755 "${D}/debian_pkg/DEBIAN/postrm"
        fi


                        ## some lintian overrides
                        mkdir -p "usr/share/lintian/overrides/"
                        cat - > "usr/share/lintian/overrides/${DEB_NAME}" <<EOFOVD
${DEB_NAME}: extended-description-is-empty
${DEB_NAME}: changelog-file-missing-in-native-package
${DEB_NAME}: debian-changelog-file-missing
EOFOVD
                        ## copyright
                        mkdir -p "usr/share/doc/${DEB_NAME}"
                        cat - <<DEB_STEP > "usr/share/doc/${DEB_NAME}/copyright"
Format-Specification: http://svn.debian.org/wsvn/dep/web/deps/dep5.mdwn?op=file&rev=135
Name: \${pkg_name}
Maintainer: ${DEB_PKG_MAINTAINER}

Files: *
Copyright: 2011 Citrix Systems.

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
                        ## udev rules
                        [ -d etc/udev/rules.d ] && mkdir -p lib/udev/rules.d && find etc/udev/rules.d -type f -exec cp '{}' lib/udev/rules.d/ ';' && rm -rf etc/udev

                        ## conffiles
                        [ -d etc ] && find etc -type f | sed -e 's/^/\//' > DEBIAN/conffiles


                        ## md5sums
                        find * -name 'DEBIAN' -prune -o -type f -exec md5sum '{}' ';' > DEBIAN/md5sums
        
        popd
        ## create package
        (
                set -e
                flock -x 220
                chown -R 0:0 "${D}"
                cd "${D}" 
                dpkg-deb --build debian_pkg 
                mv "${D}/debian_pkg.deb" "\${pkg_fname}" 
        )  220> ${CHOWN_LCK_FILE}

        ## debian validate package 
        ( set -e; cd "${D}" && lintian --allow-root "\${pkg_fname}" )
        exit_status=\$?
EOSF
            chmod 755 "${S}/oe_do_install.sh"
            sudo cp -a "${S}/oe_do_install.sh" "${build}/${S}/"
            my_chroot "${build}" "${arch}" "${S}/oe_do_install.sh" "${suite}" "${arch}" "${deb_no}" "0"
            [[ -d "${D}/packages/${suite}/${arch}" ]] || sudo mkdir -p "${D}/packages/${suite}/${arch}"
            sudo cp "${build}/${D}"/*.deb "${D}/packages/${suite}/${arch}"
            sudo chown -R ${BUILD_UID} "${D}/packages" 
	    sudo mkdir -p "${DEB_REPO_DIR}/packages/${suite}/"
	    sudo chown -R ${BUILD_UID} "${DEB_REPO_DIR}/packages/${suite}/"
	    cp -a "${build}/${D}"/*.deb "${DEB_REPO_DIR}/packages/${suite}/"
        done
    done
}

