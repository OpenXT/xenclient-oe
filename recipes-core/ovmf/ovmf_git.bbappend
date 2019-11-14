FILESEXTRAPATHS_prepend := "${THISDIR}/ovmf:"

SRC_URI = "git://github.com/tianocore/edk2.git;branch=master \
    file://0002-ovmf-update-path-to-native-BaseTools.patch \
    file://0003-BaseTools-makefile-adjust-to-build-in-under-bitbake.patch \
    https://downloadmirror.intel.com/29166/eng/PREBOOT.EXE;unpack=0;name=PREBOOT \
    "

SRCREV="dd4cae4d82c7477273f3da455084844db5cca0c0"

# PREBOOT.EXE, OS independent, latest version (currently 24.2).
SRC_URI[PREBOOT.md5sum] = "f01a69fdf39511d47a1ba2860ef177c1"
SRC_URI[PREBOOT.sha256sum] = "090137bc8af0b05c1ae2b27b8b7851d295cb1225880fb4657c7ddc470b261485"

FILES_${PN} += "\
    /usr/share/firmware/ovmf.bin \
    "

# This is mostly a copy-paste from the upstream recipe but we need to force
# OVMF_ARCH to be X64 and there is no easier way to do that. We omit a bunch
# of symlinks and SecureBoot related logic that are just not applicable.
do_compile_class-target() {
    export LFLAGS="${LDFLAGS}"
    OVMF_ARCH="X64"

    mkdir -p ${S}/Intel3.5/EFIX64/
    /usr/bin/unzip -p ${WORKDIR}/PREBOOT.EXE APPS/EFI/EFIx64/E3522X2.EFI > ${S}/Intel3.5/EFIX64/E3522X2.EFI

    # The build for the target uses BaseTools/Conf/tools_def.template
    # from ovmf-native to find the compiler, which depends on
    # exporting HOST_PREFIX.
    export HOST_PREFIX="${HOST_PREFIX}"

    # BaseTools/Conf gets copied to Conf, but only if that does not
    # exist yet. To ensure that an updated template gets used during
    # incremental builds, we need to remove the copy before we start.
    rm -f `ls ${S}/Conf/*.txt | grep -v ReadMe.txt`

    # ${WORKDIR}/ovmf is a well-known location where do_install and
    # do_deploy will be able to find the files.
    rm -rf ${WORKDIR}/ovmf
    mkdir ${WORKDIR}/ovmf
    OVMF_DIR_SUFFIX="X64"
    FIXED_GCCVER=$(fixup_target_tools ${GCC_VER})
    bbnote FIXED_GCCVER is ${FIXED_GCCVER}
    build_dir="${S}/Build/Ovmf$OVMF_DIR_SUFFIX/RELEASE_${FIXED_GCCVER}"

    bbnote "Building without Secure Boot."
    rm -rf ${S}/Build/Ovmf$OVMF_DIR_SUFFIX
    ${S}/OvmfPkg/build.sh $PARALLEL_JOBS -a $OVMF_ARCH -b RELEASE -t ${FIXED_GCCVER} -D E1000_ENABLE
    ln ${build_dir}/FV/OVMF.fd ${WORKDIR}/ovmf/ovmf.fd
}

do_install_class-target() {
    install -d ${D}/usr/share/firmware/
    install -m 0600 ${WORKDIR}/ovmf/ovmf.fd ${D}/usr/share/firmware/ovmf.bin
}

do_deploy_class-target() {
    :
}
