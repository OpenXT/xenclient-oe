SUMMARY = "64-bit shim"
DESCRIPTION = "shim is a trivial EFI application that, when run, \
attempts to open and execute another application. It will initially \
attempt to do this via the standard EFI LoadImage() and StartImage() \
calls. If these fail (because secure boot is enabled and the binary \
is not signed with an appropriate key, for instance) it will then \
validate the binary against a built-in certificate. If this succeeds \
and if the binary or signing key are not blacklisted then shim will \
relocate and execute the binary."
HOMEPAGE = "https://github.com/tklengyel/shim/shim.git"
SECTION = "bootloaders"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=b92e63892681ee4e8d27e7a7e87ef2bc"

DEPENDS += "\
    gnu-efi-native openssl util-linux-native openssl-native \
"

PV = "14+git${SRCPV}"

SRC_URI = " \
    git://github.com/rhboot/shim;protocol=https;branch=master \
    file://0001-Add-KEEP_DISCARDABLE_RELOC-build-option.patch \
    file://0002-Make-EFI_INCLUDE-path-configurable-during-make.patch \
    file://0003-Add-Measure-function-to-the-shim-lock-protocol.patch \
    file://0004-Don-t-overwrite-LoadOptions-if-NULL.patch \
    file://0005-Allow-32-bit-images-for-measurement-verification-via.patch \
    file://0006-Don-t-measure-with-Authenticode.patch \
"

SRCREV = "6c8d08c0af4768c715b79c8ec25141d56e34f8b4"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "\
    CROSS_COMPILE="${TARGET_PREFIX}" \
    prefix="${STAGING_DIR_HOST}/${prefix}" \
    LIB_GCC="`${CC} -print-libgcc-file-name`" \
    LIB_PATH="${STAGING_LIBDIR_NATIVE}" \
    EFI_PATH="${STAGING_LIBDIR_NATIVE}" \
    EFI_INCLUDE="${STAGING_INCDIR_NATIVE}/efi" \
    RELEASE="_${DISTRO}_${DISTRO_VERSION}" \
    DEFAULT_LOADER=\\\\\\xen.efi \
    OPENSSL=${STAGING_BINDIR_NATIVE}/openssl \
    HEXDUMP=${STAGING_BINDIR_NATIVE}/hexdump \
    PK12UTIL=${STAGING_BINDIR_NATIVE}/pk12util \
    CERTUTIL=${STAGING_BINDIR_NATIVE}/certutil \
    AR=${AR} \
    ARCH=x86_64 \
    KEEP_DISCARDABLE_RELOC=1 \
    REQUIRE_TPM=1 \
    ALLOW_32BIT_KERNEL_ON_X64=1 \
"

COMPATIBLE_HOST = 'i686-oe-linux|(x86_64.*).*-linux|aarch64.*-linux'

do_install() {
    install -d ${D}/boot/
    install -m 0600 "${B}/shimx64.efi" ${D}/boot/
    install -m 0600 "${B}/fbx64.efi" ${D}/boot/
}

FILES_${PN} += "\
    /boot/shimx64.efi \
    /boot/fbx64.efi \
"

BBCLASSEXTEND="native"
