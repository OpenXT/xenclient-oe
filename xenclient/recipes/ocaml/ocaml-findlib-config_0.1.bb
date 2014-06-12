LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r0"

SRC_URI = "file://findlib.conf"
S = "${WORKDIR}"


do_configure() {
}

do_compile() {
}

do_install() {
	install -d "${D}/${sysconfdir}"
	install findlib.conf "${D}/${sysconfdir}"
}

sysroot_stage_all_append() {
        sysroot_stage_dir "${D}/${sysconfdir}" "${SYSROOT_DESTDIR}/${sysconfdir}"
}


sysroot_replace_config_paths() {
	sed -i -e "s#DESTDIR#${ocamllibdir}#" "${SYSROOT_DESTDIR}${sysconfdir}/findlib.conf"
	sed -i -e "s#PATH#${OCAML_STDLIBDIR}:${STAGING_LIBDIR_OCAML}#" "${SYSROOT_DESTDIR}/${sysconfdir}/findlib.conf"
}

SYSROOT_PREPROCESS_FUNCS += "sysroot_replace_config_paths"
