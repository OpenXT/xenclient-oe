SUMMARY = "Set of tools for TPM 1.2 administration and diagnostic."
DESCRIPTION = "The tpm-tools package contains commands to allow the platform \
administrator the ability to manage and diagnose the platform's TPM. \
Additionally, the package contains commands to utilize some of the capabilities \
available in the TPM PKCS#11 interface implemented in the openCryptoki \
project."

DEPENDS = "trousers"
LICENSE = "CPL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=059e8cd6165cb4c31e351f2b69388fd9"

SRC_URI = " \
    http://downloads.sourceforge.net/trousers/${BPN}-${PV}.tar.gz;subdir=${BPN}-${PV} \
    file://tpm-tools-extendpcr-tool.patch;patch=1 \
    file://tpm-tools-standalone.patch;patch=1 \
    file://tpm-tools-quote.patch;patch=1 \
    file://tpm-tools-quote-standalone.patch;patch=1 \
    file://tpm-tools-sealdata-pcrval.patch;patch=1 \
    file://tpm-tools-gcc6-compilation.patch;patch=1 \
    file://tpm-tools-passwd-stdin.patch;patch=1 \
    file://revert-6fb8a3c-fix-segv.patch;patch=1 \
    file://tpm-tools-setenable-stdin.patch \
    file://tpm-tools-openssl-1.1.0.patch \
    file://tpm-tools-gcc9-compilation.patch \
"
SRC_URI[md5sum] = "03839674d51eb07437eccca8fd3bab3d"
SRC_URI[sha256sum] = "ea126c5cd2ada56beb5118a141a498053f2d85f56263d215784f0ed86fff4213"

S = "${WORKDIR}/${BPN}-${PV}"

inherit gettext autotools-brokensep

PACKAGES =+ "${PN}-sa"
FILES_${PN}-sa = " \
    ${bindir}/tpm_extendpcr_sa \
    ${bindir}/tpm_sealdata_sa \
    ${bindir}/tpm_unsealdata_sa \
    ${bindir}/tpm_quote_sa \
"

do_install_append() {
    rm ${D}${sbindir}/tpm_clear
}
