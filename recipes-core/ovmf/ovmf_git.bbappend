SRC_URI += " \
    https://downloadmirror.intel.com/29137/eng/PREBOOT.EXE;unpack=0;name=PREBOOT \
"
DEPENDS_append += " \
    unzip-native \
"

# PREBOOT.EXE, OS independent, latest version (currently 24.3).
SRC_URI[PREBOOT.md5sum] = "8660641e184dafdeb78b8ca1fbd837f7"
SRC_URI[PREBOOT.sha256sum] = "83dac749d74a6a54d7451bee79f9e1d605c4e2775d6b524d39030b248989092a"

do_extract_bootutil() {
    mkdir -p "${S}/Intel3.5/EFIX64"
    unzip -q -p "${WORKDIR}/PREBOOT.EXE" "APPS/EFI/EFIx64/E3522X2.EFI" > "${S}/Intel3.5/EFIX64/E3522X2.EFI"
}
addtask do_extract_bootutil before do_configure after do_unpack
do_extract_bootutil[doc] = "Extract Intel's proprietary E1000 NIC driver to be embedded in OVMF image."
do_extract_bootutil[depends] = "${PN}:do_prepare_recipe_sysroot"
do_extract_bootutil[dirs] = "${B}"

do_compile_class-target_append() {
    bbnote "Building with E1000 (support for netboot)."
    rm -rf ${S}/Build/Ovmf$OVMF_DIR_SUFFIX
    ${S}/OvmfPkg/build.sh $PARALLEL_JOBS -a $OVMF_ARCH -b RELEASE -t ${FIXED_GCCVER} -D E1000_ENABLE
    ln ${build_dir}/FV/OVMF.fd ${WORKDIR}/ovmf/ovmf.e1000.fd
    ln ${build_dir}/FV/OVMF_CODE.fd ${WORKDIR}/ovmf/ovmf.e1000.code.fd
}

do_install_class-target_append() {
    install -d ${D}${datadir}/firmware
    install -m 0600 ${WORKDIR}/ovmf/ovmf.e1000.fd ${D}${datadir}/firmware/ovmf.e1000.bin
    ln -sf ovmf.e1000.bin ${D}${datadir}/firmware/ovmf.bin
}

PACKAGES += " \
    ${PN}-firmware \
"

FILES_${PN}-firmware += " \
    ${datadir}/firmware \
"
