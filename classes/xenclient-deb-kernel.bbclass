inherit xenclient-deb-utils

DEB_SECTION ?= "kernel"
DEB_DESC_EXT ?= "${DEB_DESC}"
DEB_PKG_MAINTAINER ?= "Citrix Systems <customerservice@citrix.com>"

CHROOT_PATH="${STAGING_DIR}/debootstrap"
STAGING_DIR_DEB="${CHROOT_PATH}"

APT_LOCK="flock -x /tmp/.lck-apt-deb"
CHOWN_LCK_FILE="/tmp/.lck-chown-deb"

PACKAGES += "kernel-debian"
FILES_kernel-debian = "/packages/*"

def getClassFdir(bb, d):
	filepath = d.getVar('FILE',1)
	idx = filepath.rfind('/recipes/')
	if idx == -1:
		return ""
	return filepath[:idx] + '/classes/xenclient-deb-kernel/'


CLASSFDIR = "${@getClassFdir(bb, d)}"

die() {
	echo "ERROR: $1"
	exit 1
}

my_chroot() {
	root="${1}"
	arch="${2}"
	shift 2

	if [[ ${arch} == 'i386' ]]; then
		sudo chroot "${root}" linux32 $*
	elif [[ ${arch} == 'amd64' ]]; then
		sudo chroot "${root}" linux64 $*
	else
	    sudo chroot $*
	fi
}

prepare_chroot() {
	local chroot="$1"
	(
		set +e 
		flock -x 210
		sudo cp -f /etc/resolv.conf "${chroot}/etc"
		[[ -d "${chroot}/dev" ]] || sudo mkdir -p "${chroot}/dev" 
		[[ -c "${chroot}/dev/null" ]] || sudo rm -f "${chroot}/dev/null"
		[[ -e "${chroot}/dev/null" ]] || ( sudo mknod "${chroot}/dev/null" c 1 3  && sudo chmod 666 "${chroot}/dev/null" )
		exit 0

	)  210> /tmp/.lck-oe-prepare_chroot
	return 0
}

update_chroot() {
    local chroot="$1"
	local arch="$2"

	( set +e;  my_chroot "${chroot}" "${arch}" ${APT_LOCK} apt-get update; exit 0 )
}

do_deb_package() {
	local deb_no=0
	local step="deb_packages"

	cat <<EOF > "${S}/oe_do_${step}.sh"
#!/bin/bash

	set -e
	set -x

	deb_suite=\$1
	deb_arch=\$2
	deb_no=\$3

	kernname=""

	die() {
		echo "ERROR: \$1"
		exit 1
	}

## linux-image deb package
	mkdir -p "${D}/deb-linux-image/debian/"
	pushd "${D}/deb-linux-image/debian/"
		[[ -d "${D}/lib/modules/" ]] || die "no ${D}/lib/modules ?"
		kernname=\$(find "${D}/lib/modules/" -maxdepth 1 -mindepth 1 -type d | head -n 1 | xargs basename) 
		[[ -z "\${kernname}" ]] && die "cannot find kernel dir in ${D}/lib/modules"

		pkg_kernelname="\${kernname}"
		pkg_name="linux-image-\${pkg_kernelname}"
		pkg_fname="\${pkg_name}-\${deb_suite}_\${deb_arch}.deb"


		rsync -ar "${D}/boot" .
		rm -f ./boot/vmlinux-*
		mkdir -p "lib/modules/\${pkg_kernelname}"
		rsync -ar "${D}/lib/modules/\${kernname}/kernel" "lib/modules/\${pkg_kernelname}/"
		#rsync -ar "${D}/lib/modules/\${kernname}/modules.order" "lib/modules/\${pkg_kernelname}/"
		mkdir -p "lib/firmware/\${pkg_kernelname}"
		rsync -ar "${D}/lib/firmware/" "lib/firmware/"

		mkdir -p "usr/share/doc/\${pkg_name}"

		## some lintian overrides
		mkdir -p "usr/share/lintian/overrides/"
			cat - > "usr/share/lintian/overrides/\${pkg_name}" <<EOFOVD
\${pkg_name}: extended-description-is-empty
\${pkg_name}: changelog-file-missing-in-native-package
\${pkg_name}: debian-changelog-file-missing
\${pkg_name}: wrong-path-for-interpreter
\${pkg_name}: shell-script-fails-syntax-check
EOFOVD

		pkg_size=\$(du -s . | cut -f1)
		mkdir -p "DEBIAN/"

		[[ -d "${CLASSFDIR}/image" ]] || die "CLASSFDIR/image, ${CLASSFDIR}/image not found"
		pushd "${CLASSFDIR}/image" 
			chmod 0755 postinst postrm preinst prerm
			chmod 0644 copyright
			cp -p postinst postrm preinst prerm "${D}/deb-linux-image/debian/DEBIAN/" 
			cp -p copyright "${D}/deb-linux-image/debian/usr/share/doc/\${pkg_name}/"
		popd

		

		cat - <<DEB_STEP > "DEBIAN/control"
Package: \${pkg_name}
Section: kernel
Source: linux-3.11
Installed-Size: \${pkg_size}
Version: \${pkg_kernelname}-\${deb_suite}
Architecture: \${deb_arch}
Maintainer: ${DEB_PKG_MAINTAINER}
Depends: module-init-tools, linux-base (>= 3~), initramfs-tools (>= 0.55)
Priority: optional
Homepage: http://www.citrix.com
Description: ${DEB_DESC}
 ${DEB_DESC_EXT}
DEB_STEP

			for file in postinst postrm preinst prerm; do
				sed -r "s/@VERSION@/\${pkg_kernelname}/" -i "DEBIAN/\${file}"
			done

			## md5sums
			find * -name 'DEBIAN' -prune -o -type f -exec md5sum '{}' ';' > DEBIAN/md5sums

	popd

	pushd "${D}/deb-linux-image/"
	(
		set -e
		flock -x 220
		chown -R 0:0 debian
		dpkg-deb --build debian && mv debian.deb "\${pkg_fname}"
		lintian --allow-root "\${pkg_fname}"
		mv "\${pkg_fname}" "${D}"
	)  220> ${CHOWN_LCK_FILE}
	popd
	rm -rf --one-file-system "${D}/deb-linux-image/"


## linux-headers deb package
	mkdir -p "${D}/deb-linux-headers/debian/"
	pushd "${D}/deb-linux-headers/debian/"

		pkg_kernelname="\${kernname}"
		pkg_name="linux-headers-\${pkg_kernelname}"
		pkg_fname="\${pkg_name}-\${deb_suite}_\${deb_arch}.deb"


		mkdir -p "lib/modules/\${pkg_kernelname}/"
		mkdir -p "usr/src/\${pkg_name}/"

		rsync -ar "${D}/kernel/arch" "usr/src/\${pkg_name}/"
		rsync -ar "${D}/kernel/include" "usr/src/\${pkg_name}/"
		rsync -ar "${D}/kernel/scripts" "usr/src/\${pkg_name}/"

		pushd "usr/src/\${pkg_name}/"
		(
			set +e;
			find . \( -type f -name '*.so' -o \( -name '*.o' -o -name '*.o.cmd' -o -name '*.so.dbg' -o -name '*.o.parts' \) \) -exec rm -f '{}' ';'
			find . -type f -executable -exec strip '{}' ';'
			find . -type f -executable \( -name 'vmlinux*' -o -name 'vmlinuz*' \) -exec rm -f '{}' ';'
			find . -type f -name '.gitignore' -exec rm -f '{}' ';'
			exit 0
		)
		popd

		(
			set +e;
			cd ${D}/kernel/
			cp -a .config* "${D}/deb-linux-headers/debian/usr/src/\${pkg_name}/"
			cp -a config*  "${D}/deb-linux-headers/debian/usr/src/\${pkg_name}/"
			exit 0
		)

		rsync -ar "${D}/kernel/Makefile" "usr/src/\${pkg_name}/"
		rsync -ar "${D}/kernel/Module.symvers" "usr/src/\${pkg_name}/"

		pushd "lib/modules/\${pkg_kernelname}/"
			rm -f ./source; ln -s "/usr/src/\${pkg_name}" ./source
			rm -f ./build; ln -s "/usr/src/\${pkg_name}" ./build
		popd

		mkdir -p "usr/share/doc/\${pkg_name}"

		## some lintian overrides
		mkdir -p "usr/share/lintian/overrides/"
			cat - > "usr/share/lintian/overrides/\${pkg_name}" <<EOFOVD
\${pkg_name}: extended-description-is-empty
\${pkg_name}: changelog-file-missing-in-native-package
\${pkg_name}: debian-changelog-file-missing
\${pkg_name}: wrong-path-for-interpreter
\${pkg_name}: shell-script-fails-syntax-check
EOFOVD

		pkg_size=\$(du -s . | cut -f1)
		mkdir -p "DEBIAN/"

		[[ -d "${CLASSFDIR}/headers" ]] || die "CLASSFDIR/headers ${CLASSFDIR}/headers not found"
		pushd "${CLASSFDIR}/headers" 
			chmod 0755 postinst
			chmod 0644 copyright
			cp -p postinst "${D}/deb-linux-headers/debian/DEBIAN/" 
			cp -p copyright "${D}/deb-linux-headers/debian/usr/share/doc/\${pkg_name}/"
		popd

		

		cat - <<DEB_STEP > "DEBIAN/control"
Package: \${pkg_name}
Source: linux-3.11
Version: \${pkg_kernelname}-\${deb_suite}
Architecture: \${deb_arch}
Maintainer: ${DEB_PKG_MAINTAINER}
Installed-Size: \${pkg_size}
Depends: gcc-4.7, python (>= 2.5), libc6 (>= 2.7)
Provides: linux-headers, linux-headers-3.11
Section: kernel
Priority: optional
Description: Header files for \${pkg_kernelname}
 This package provides the architecture-specific kernel header files for
 Linux kernel \${pkg_kernelname}, generally used for building out-of-tree
 kernel modules.  These files are going to be installed into
 /usr/src/\${pkg_kernelname}, and can be used for building
 modules that load into the kernel provided by the
 \${pkg_kernelname} package.
DEB_STEP

			for file in postinst; do
				sed -r "s/@VERSION@/\${pkg_kernelname}/" -i "DEBIAN/\${file}"
			done

			## md5sums
			find * -name 'DEBIAN' -prune -o -type f -exec md5sum '{}' ';' > DEBIAN/md5sums
	popd

	pushd "${D}/deb-linux-headers/"
	(
		set -e
		flock -x 221
		chown -R 0:0 debian
		dpkg-deb --build debian && mv debian.deb "\${pkg_fname}"
		lintian --allow-root "\${pkg_fname}"
		mv "\${pkg_fname}" "${D}"
	)  221> ${CHOWN_LCK_FILE}
	popd
	rm -rf --one-file-system "${D}/deb-linux-headers/"

EOF

	chmod +x "${S}/oe_do_${step}.sh"
	[[ -z "${XCT_DEB_PKGS_DIR}" ]] && die "XCT_DEB_PKGS_DIR is not set !"
	mkdir -p "${XCT_DEB_PKGS_DIR}"
    for suite in ${DEB_SUITE}
    do
        for arch in ${DEB_ARCH}
        do
			(( deb_no = ${deb_no} + 1 ))
            build="${CHROOT_PATH}/${suite}_${arch}"
            prepare_chroot "${build}" "${arch}"
            sudo mkdir -p "${build}/${S}/"
			sudo cp -p "${S}/oe_do_${step}.sh" "${build}/${S}/"
			sudo rsync -ar "${D}/" "${build}/${D}"

			if ! [[ -z "${CLASSFDIR}" ]]; then
				sudo mkdir -p "${build}/${CLASSFDIR}"
				sudo rsync -ar "${CLASSFDIR}/" "${build}/${CLASSFDIR}/"
				sudo chown -R "${BUILD_UID}" "${build}/${CLASSFDIR}" 
			fi
			sudo chown -R "${BUILD_UID}" "${build}/${S}" 
			my_chroot "${build}" "${arch}" "${S}/oe_do_${step}.sh" "${suite}" "${arch}" "${deb_no}" 
			[[ -d "${D}/packages/${suite}/${arch}" ]] || sudo mkdir -p "${D}/packages/${suite}/${arch}"
			sudo cp "${build}/${D}"/*.deb "${D}/packages/${suite}/${arch}"
			sudo chown -R "${BUILD_UID}" "${D}" 
		     	sudo mkdir -p "${DEB_REPO_DIR}/packages/${suite}"
			sudo chown -R "${BUILD_UID}" "${DEB_REPO_DIR}/packages"
			cp -a "${D}/packages/${suite}/${arch}"/*.deb "${DEB_REPO_DIR}/packages/${suite}/"
		done
	done
}
addtask deb_package before do_package after do_deploy

