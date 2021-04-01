do_image_qa_module_sigs() {
	local bzImage="${IMAGE_ROOTFS}/boot/bzImage"
	if [ ! -f "$bzImage" ]; then
		bzImage="${DEPLOY_DIR_IMAGE}/bzImage"
	fi

	if [ ! -f "$bzImage" ]; then
		bberror "Cannot find bzImage in rootfs or DEPLOYDIR"
		exit 1
	fi

	local vers
	# -L to follow the symlink for deploy dir
	vers="$( file -L "$bzImage" | sed 's/.*version \([^ ]*\) .*/\1/' )"

	if [ ! -d "${IMAGE_ROOTFS}/lib/modules/$vers" ]; then
		bbnote "bzImage version is $vers, but module directory is missing"
		exit 0
	fi

	local builddir="${STAGING_KERNEL_BUILDDIR}"
	local scriptdir="${STAGING_KERNEL_DIR}/scripts"

	local vmlinux
	local keyring
	vmlinux="$( mktemp -t "vmlinux-$vers.XXXXXXXX" )"
	keyring="$( mktemp -t "keyring-$vers.XXXXXXXX" )"

	# Extract vmlinux from bzImage
	"$scriptdir/extract-vmlinux" "$bzImage" > "$vmlinux" 2>/dev/null

	# Extract keyring from vmlinux (needs System.map since bzImage doesn't
	# have symbols
	if ! output=$( "$scriptdir/extract-sys-certs.pl" \
			-s "$builddir/System.map-$vers" "$vmlinux" \
			"$keyring" 2>&1 1>/dev/null ) ; then
		if expr "$output" : "^Can't find system certificate list " >/dev/null
		then
			bbnote "No certificate list - skipping"
			exit 0
		else
			bberror "$output"
			exit 1
		fi
	fi

	# We need a PEM for smime -certfile option
	openssl x509 -in "$keyring" -inform DER -out "$keyring.pem" -outform PEM
	mv "$keyring.pem" "$keyring"

	ret=0
	# For loop so setting ret is in the main shell, and bitbake doesn't
	# support `< <( find )`
	for mod in $( find "${IMAGE_ROOTFS}/lib/modules/$vers/" -name "*.ko" )
	do
		if ! check_module "$scriptdir" "$keyring" "$mod" ; then
			bberror "Incorrect signature on module $mod"
			ret=1;
			break
		fi
	done

	rm "$keyring"
	rm "$vmlinux"

	return "$ret"
}

check_module() {
	local scriptdir="$1"
	local keyring="$2"
	local mod="$3"
	local basename
	local sigdata
	local data
	basename="$( basename "$mod" )"
	data="$( mktemp -t "$basename.data.XXXXXXXX" )"
	sigdata="$( mktemp -t "$basename.sigdata.XXXXXXXX" )"

	"$scriptdir/extract-module-sig.pl" -s "$mod" > "$sigdata" 2>/dev/null
	"$scriptdir/extract-module-sig.pl" -0 "$mod" > "$data" 2>/dev/null

	# needed to get smime failure and not sed's exit status
	set -o pipefail
	# Verify
	openssl smime -verify -binary -inform DER -in "$sigdata" \
		-content "$data" -certfile "$keyring" -nointern -noverify \
		-out /dev/null 2>&1 >/dev/null | \
			sed -n '/Verification successful/!p'
	ret=$?

	rm "$data"
	rm "$sigdata"

	return "$ret"
}

do_image_qa[depends] += " \
	perl-native:do_populate_sysroot \
	openssl-native:do_populate_sysroot \
	virtual/kernel:do_shared_workdir \
"

IMAGE_QA_COMMANDS += " \
	do_image_qa_module_sigs \
"
