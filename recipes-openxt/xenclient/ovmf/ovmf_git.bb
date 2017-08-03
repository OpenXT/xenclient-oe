DESCRIPTION = "OVMF - UEFI firmware for Qemu and KVM"
HOMEPAGE = "http://sourceforge.net/apps/mediawiki/tianocore/index.php?title=OVMF"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://OvmfPkg/License.txt;md5=343dc88e82ff33d042074f62050c3496"

SRC_URI = " \
	git://github.com/tianocore/edk2.git;branch=master \
  	https://ftp.openssl.org/source/old/1.0.2/openssl-1.0.2f.tar.gz;unpack=0 \
	file://0001-BaseTools-Force-tools-variables-to-host-toolchain.patch \
	file://0001-OvmfPkg-Don-t-build-in-QemuVideoDxe-when-we-have-CSM.patch \
	file://0001-pick-up-any-display-device-not-only-vga.patch \
	file://0001-OvmfPkg-don-t-lock-lock-umb-when-running-csm.patch \
	file://0001-MdeModulePkg-TerminalDxe-add-other-text-resolutions.patch \
	file://0001-EXCLUDE_SHELL_FROM_FD.patch \
	file://0001-OvmfPkg-SmbiosPlatformDxe-install-legacy-QEMU-tables.patch \
	file://0002-OvmfPkg-SmbiosPlatformDxe-install-patch-default-lega.patch \
	file://0003-OvmfPkg-SmbiosPlatformDxe-install-patch-default-lega.patch \
	file://0001-OvmfPkg-EnrollDefaultKeys-application-for-enrolling-.patch \
	file://0001-tools_def.template-take-GCC4-_-IA32-X64-prefixes-fro.patch \
	file://0001-OvmfPkg-disable-multi-processor-support-for-boot-tim.patch \
	file://ovmf-q35-xen-support.patch \
"


SRC_URI[sha256sum] = "932b4ee4def2b434f85435d9e3e19ca8ba99ce9a065a61524b429a9d5e9b2e9c"
SRC_URI[md5sum] = "b3bf73f507172be9292ea2a8c28b659d"

SRCREV="196ccda08fc481dae4fc97db8f2938df87801edb"

S = "${WORKDIR}/git"

DEPENDS_class-native="util-linux-native iasl-native"

# OVMF has trouble building with the default optimization of -O2.
BUILD_OPTIMIZATION="-pipe"

# OVMF supports IA only, although it could conceivably support ARM someday.
COMPATIBLE_HOST='(i.86|x86_64).*'

do_patch_append() {
    bb.build.exec_func('do_fix_iasl', d)
    bb.build.exec_func('do_fix_toolchain', d)
    bb.build.exec_func('do_inject_openssl', d)
}

do_inject_openssl() {
    tar -C ${S}/CryptoPkg/Library/OpensslLib -xf ${WORKDIR}/openssl-1.0.2f.tar.gz
    (cd ${S}/CryptoPkg/Library/OpensslLib/openssl-1.0.2f && patch -p1 < ../EDKII_openssl-1.0.2f.patch)
    (cd ${S}/CryptoPkg/Library/OpensslLib && ./Install.sh )
}

do_fix_iasl() {
    sed -i -e 's#/usr/bin/iasl#${STAGING_BINDIR_NATIVE}/iasl#' ${S}/BaseTools/Conf/tools_def.template
}

do_fix_toolchain(){
    sed -i -e 's#DEF(ELFGCC_BIN)/#${TARGET_PREFIX}#' ${S}/BaseTools/Conf/tools_def.template
    sed -i -e 's#DEF(GCC.*PREFIX)#${TARGET_PREFIX}#' ${S}/BaseTools/Conf/tools_def.template
    sed -i -e 's#${TARGET_PREFIX}make#make#g' ${S}/BaseTools/Conf/tools_def.template
    sed -i -e "s#^LINKER\(.*\)#LINKER\1\nLFLAGS += ${BUILD_LDFLAGS}#" ${S}/BaseTools/Source/C/Makefiles/app.makefile
    sed -i -e "s#^LINKER\(.*\)#LINKER\1\nCFLAGS += ${BUILD_CFLAGS}#" ${S}/BaseTools/Source/C/Makefiles/app.makefile
    sed -i -e "s#^LINKER\(.*\)#LINKER\1\nLFLAGS += ${BUILD_LDFLAGS}#" ${S}/BaseTools/Source/C/VfrCompile/GNUmakefile
    sed -i -e "s#^LINKER\(.*\)#LINKER\1\nCFLAGS += ${BUILD_CFLAGS}#" ${S}/BaseTools/Source/C/VfrCompile/GNUmakefile
}

GCC_VER="$(${CC} -v 2>&1 | tail -n1 | awk '{print $3}' | awk -F. '{print $1$2}')"

do_compile() {

    export LFLAGS="${LDFLAGS}"
    OVMF_ARCH="X64"

    (
        cd ${S}

        . ./edksetup.sh

        if [ ! -f basetools.stamp ]; then
          make -C BaseTools && touch basetools.stamp
        fi

        GCCVER=$(${CC} --version | awk '{ print $3; exit}')
        case "$GCCVER" in
            4.4*)   CC_FLAGS="-t GCC44";;
            4.5*)   CC_FLAGS="-t GCC45";;
            4.6*)   CC_FLAGS="-t GCC46";;
            4.7*)   CC_FLAGS="-t GCC47";;
            4.8*)   CC_FLAGS="-t GCC48";;
            4.9*)   CC_FLAGS="-t GCC49";;
            5.0*)   CC_FLAGS="-t GCC49";;
            5.1*)   CC_FLAGS="-t GCC49";;
            5.2*)   CC_FLAGS="-t GCC49";;
            5.3*)   CC_FLAGS="-t GCC49";;
        esac


        OVMF_FLAGS="$CC_FLAGS -D HTTP_BOOT_ENABLE"

        build $OVMF_FLAGS -a X64 -p OvmfPkg/OvmfPkgX64.dsc
    )
}

do_install() {

        GCCVER=$(${CC} --version | awk '{ print $3; exit}')
        case "$GCCVER" in
            4.4*)   OUTPATH="DEBUG_GCC44";;
            4.5*)   OUTPATH="DEBUG_GCC45";;
            4.6*)   OUTPATH="DEBUG_GCC46";;
            4.7*)   OUTPATH="DEBUG_GCC47";;
            4.8*)   OUTPATH="DEBUG_GCC48";;
            4.9*)   OUTPATH="DEBUG_GCC49";;
            5.0*)   OUTPATH="DEBUG_GCC49";;
            5.1*)   OUTPATH="DEBUG_GCC49";;
            5.2*)   OUTPATH="DEBUG_GCC49";;
            5.3*)   OUTPATH="DEBUG_GCC49";;
        esac


    build_dir="${S}/Build/OvmfX64/${OUTPATH}"

    install -d ${D}${datadir}/ovmf
    install -m 0755 ${build_dir}/FV/OVMF.fd \
	${D}${datadir}/ovmf/OVMF.fd
    install -m 0755 ${build_dir}/FV/OVMF_VARS.fd \
	${D}${datadir}/ovmf/OVMF_VARS.fd
    install -m 0755 ${build_dir}/X64/EnrollDefaultKeys.efi \
	${D}${datadir}/ovmf/EnrollDefaultKeys.efi

#    install -d ${STAGING_DATADIR}/ovmf
#    install -m 0755 ${build_dir}/FV/OVMF.fd \
#	${STAGING_DATADIR}/ovmf/OVMF.fd
#    install -m 0755 ${build_dir}/FV/OVMF_VARS.fd \
#	${STAGING_DATADIR}/ovmf/OVMF_VARS.fd

}

BBCLASSEXTEND = "native"
