do_install_append() {
	# Change the shell to false for everybody but root
	sed -i '/^root:/b; s#^\(.*\):/bin/[a-z]\+$#\1:/bin/false#' ${D}${datadir}/base-passwd/passwd.master
}
