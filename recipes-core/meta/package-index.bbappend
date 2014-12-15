# Since we switched to opkg-make-index version that does not generate Packages.filelist
# by default, but we probably still want to have it in the final package feed let's
# make package-index (which is called after extra packages) to generate it
#
# package_update_index_ipk_full is taken directly from package_ipk.bbclass 
# except the extra -l

package_update_index_ipk_full () {
	set -x

	ipkgarchs="${ALL_MULTILIB_PACKAGE_ARCHS} ${SDK_PACKAGE_ARCHS}"

	if [ ! -z "${DEPLOY_KEEP_PACKAGES}" ]; then
		return
	fi

	packagedirs="${DEPLOY_DIR_IPK}"
	for arch in $ipkgarchs; do
		packagedirs="$packagedirs ${DEPLOY_DIR_IPK}/$arch"
	done

	multilib_archs="${MULTILIB_ARCHS}"
	for arch in $multilib_archs; do
		packagedirs="$packagedirs ${DEPLOY_DIR_IPK}/$arch"
	done

	for pkgdir in $packagedirs; do
		if [ -e $pkgdir/ ]; then
			touch $pkgdir/Packages
			flock $pkgdir/Packages.flock -c "opkg-make-index -l $pkgdir/Packages.filelist -r $pkgdir/Packages -p $pkgdir/Packages -m $pkgdir/"
		fi
	done
}

PACKAGEINDEXES = "package_update_index_ipk_full;"
