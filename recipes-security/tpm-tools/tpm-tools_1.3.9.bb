SRC_URI[md5sum] = "03839674d51eb07437eccca8fd3bab3d"
SRC_URI[sha256sum] = "ea126c5cd2ada56beb5118a141a498053f2d85f56263d215784f0ed86fff4213"
DEPENDS = "trousers"
LICENSE = "CPL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=059e8cd6165cb4c31e351f2b69388fd9"

SRC_URI = "http://downloads.sourceforge.net/trousers/${PN}-${PV}.tar.gz;subdir=${PN}-${PV} \
           file://tpm-tools-extendpcr-tool.patch;patch=1 \
           file://tpm-tools-standalone.patch;patch=1 \
           file://tpm-tools-quote.patch;patch=1 \
           file://tpm-tools-quote-standalone.patch;patch=1 \
           file://tpm-tools-sealdata-pcrval.patch;patch=1 \
           file://tpm-tools-gcc6-compilation.patch;patch=1 \
           file://tpm-tools-passwd-stdin.patch;patch=1 \
           file://revert-6fb8a3c-fix-segv.patch;patch=1 \
           file://tpm-tools-setenable-stdin.patch \
"

# This version is packaged without root directory.
S = "${WORKDIR}/${PN}-${PV}"

inherit autotools gettext autotools-brokensep

PACKAGES =+ "${PN}-sa"
FILES_${PN}-sa = "/usr/bin/tpm_extendpcr_sa"
FILES_${PN}-sa += "/usr/bin/tpm_sealdata_sa"
FILES_${PN}-sa += "/usr/bin/tpm_unsealdata_sa"
FILES_${PN}-sa += "/usr/bin/tpm_quote_sa"

do_unpack_prepend() {
}

do_install_append() {
    rm ${D}/usr/sbin/tpm_clear
}
