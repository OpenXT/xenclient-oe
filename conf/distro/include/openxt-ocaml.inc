
SYSROOT_OCAML_PATH = "${STAGING_DIR_NATIVE}${libdir_native}/${TRANSLATED_TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}/ocaml"
export OCAML_STDLIBDIR = "${SYSROOT_OCAML_PATH}/site-lib"
OCAML_HEADERS = "${SYSROOT_OCAML_PATH}"
export ocamllibdir = "${libdir}/ocaml"
export STAGING_LIBDIR_OCAML = "${STAGING_LIBDIR}/ocaml"
OCAML_FINDLIB_CONF = "${STAGING_DIR_HOST}${sysconfdir}/findlib.conf"

