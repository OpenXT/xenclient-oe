DEB_REPO_DIR="${TMPDIR}/deb-xctools-image/"

fakeroot do_install() {
	set -x

	( set +e; sudo rm -rf --one-file-system "${DEB_REPO_DIR}/debian"; exit 0 )
	sudo mkdir -p "${DEB_REPO_DIR}/debian/conf/"
	sudo chown -R ${BUILD_UID} "${DEB_REPO_DIR}/debian"

##	find "${DEB_REPO_DIR}/packages/" -type f -name '*.deb' -exec \
##		dpkg-sig -k keyid --sign builder '{}' ';'

	[[ -z "${DISTROS}" ]] && ( echo " ** please set DISTROS variable"; exit 1; )
	touch "${DEB_REPO_DIR}/debian/conf/distributions"
	deb_dist_no=0
	for distro in ${DISTROS}; do
		(( deb_dist_no = ${deb_dist_no} + 1 ))
		cat <<EOF >> "${DEB_REPO_DIR}/debian/conf/distributions"
Origin: Xctools
Label: XC Tools
Codename: ${distro}
Architectures: i386 amd64
Components: main
Description: Apt repository for XenClient Tools

EOF	

		xc_debdir=$(mktemp -d)
		find "${DEB_REPO_DIR}/packages/${distro}" -type f -name '*.deb' | \
			while read debpath; do
				(
					pkg_name=$(dpkg --info "${debpath}" | sed -n -re 's/^\s*Package:\s*([^ \t]+)\s*$/\1/p')
					( set +e; echo -n "${pkg_name}" | egrep -q '\-dev$' ) || echo "${pkg_name}" >> "${xc_debdir}/deps.tmp"
					cd "${DEB_REPO_DIR}/debian/" &&	reprepro includedeb "${distro}" "${debpath}"
				)
			done
		xc_deps="$(sort -u "${xc_debdir}/deps.tmp" | tr '\n' ' ' | sed -re 's/^\s+//g; s/\s+$//g; s/( +)/, /g')"
		deb_fname="${DEB_NAME}-${PV}-${deb_dist_no}.deb"
		rm -f "${xc_debdir}/deps.tmp"

		mkdir -p "${xc_debdir}/debian/"
		pushd "${xc_debdir}/debian/"
			mkdir "DEBIAN/"

			## control
			cat - <<EOF > "DEBIAN/control"
Package: ${DEB_NAME}
Section: ${DEB_SECTION}
Version: ${PV}-${deb_dist_no}
Architecture: all
Maintainer: ${DEB_PKG_MAINTAINER}
Depends: ${xc_deps}
Priority: optional
Homepage: http://www.citrix.com
Description: ${DEB_DESC}
 ${DEB_DESC_EXT}
EOF


	## postinst
	[ -z "${XENCLIENT_TOOLS}" ] && ( set -e; echo "error: XENCLIENT_TOOLS is empty"; exit 1 )
	XCT_MAJORVERSION=$(echo "${XENCLIENT_TOOLS}" | cut -d. -f1)
	XCT_MINORVERSION=$(echo "${XENCLIENT_TOOLS}" | cut -d. -f2)
	XCT_MICROVERSION=$(echo "${XENCLIENT_TOOLS}" | cut -d. -f3)
	XCT_BUILDVERSION=$(echo "${XENCLIENT_TOOLS}" | cut -d. -f4)
	cat - <<EOF > "DEBIAN/postinst"
#!/bin/sh

	set -e

	die() {
		echo "Failed: \$1"
		exit 1
	}

	[ -d /proc/xen/ ] || ( set +e; modprobe xc_xen ; modprobe xc_xenfs; exit 0 )
	[ -e /proc/xen/xenbus ] || mount -t xenfs nodev /proc/xen/

	xenstore-exists domid || die "cannot communicate with xenstore"
	xenstore-exists attr || xenstore-write "attr"
	xenstore-exists "attr/PVAddons" || xenstore-write "attr/PVAddons" ""
	xenstore-write "attr/PVAddons/Installed"  "1"
	xenstore-write "attr/PVAddons/MajorVersion"  "${XCT_MAJORVERSION}"
	xenstore-write "attr/PVAddons/MinorVersion"  "${XCT_MINORVERSION}"
	xenstore-write "attr/PVAddons/MicroVersion"  "${XCT_MICROVERSION}"
	xenstore-write "attr/PVAddons/BuildVersion"  "${XCT_BUILDVERSION}"

	exit 0
EOF
			chmod 755 "DEBIAN/postinst"

			## postrm
			cat - <<EOF > "DEBIAN/postrm"
#!/bin/sh

	set -e

	case "\$1" in
		remove|purge)

		[ -d /proc/xen/ ] || ( set +e; modprobe xc_xen ; modprobe xc_xenfs; exit 0 )
		[ -e /proc/xen/xenbus ] || mount -t xenfs nodev /proc/xen/
		xenstore-exists domid || die "cannot communicate with xenstore"
		xenstore-exists attr || xenstore-write "attr"
		xenstore-exists "attr/PVAddons" || xenstore-write "attr/PVAddons" ""
		xenstore-write "attr/PVAddons/Installed"  "0"
		;;

		upgrade|failed-upgrade|abort-install|abort-upgrade|disappear)

		;;

		*)
			exit 0
		;;

	esac
EOF

			chmod 755 "DEBIAN/postrm"

			crt_year=$(date '+%Y')
			mkdir -p "usr/share/doc/${DEB_NAME}"
			cat - <<EOF > "usr/share/doc/${DEB_NAME}/copyright"
Format-Specification: http://svn.debian.org/wsvn/dep/web/deps/dep5.mdwn?op=file&rev=135
Name: ${DEB_NAME}
Maintainer: ${DEB_PKG_MAINTAINER}

Files: *
Copyright: ${crt_year} Citrix Systems.

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

EOF
			## some lintian overrides
			mkdir -p "usr/share/lintian/overrides/"
			cat - > "usr/share/lintian/overrides/${DEB_NAME}" <<EOF
${DEB_NAME}: extended-description-is-empty
${DEB_NAME}: changelog-file-missing-in-native-package
${DEB_NAME}: non-dev-pkg-with-shlib-symlink
${DEB_NAME}: debian-changelog-file-missing
EOF
			## md5sums
			find * -name 'DEBIAN' -prune -o -type f -exec md5sum '{}' ';' > DEBIAN/md5sums
		popd

		( set -e; cd "${xc_debdir}" && chown -R 0:0 debian && dpkg-deb --build debian && mv debian.deb "${deb_fname}" && lintian --allow-root "${deb_fname}" )
		cd "${DEB_REPO_DIR}/debian/" && reprepro includedeb "${distro}" "${xc_debdir}/${deb_fname}"
		rm -rf "${xc_debdir}"

	done

	(
		set +e
		cd "${DEB_REPO_DIR}"
		ls -A -1 . | \
		while read dir; do
			[[ "${dir}" == 'debian' ]] && continue
			rm -rf "${dir}"
		done
		exit 0
	)
	
}
