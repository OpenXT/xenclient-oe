inherit gettext
SRC_URI[md5sum] = "03839674d51eb07437eccca8fd3bab3d"
SRC_URI[sha256sum] = "ea126c5cd2ada56beb5118a141a498053f2d85f56263d215784f0ed86fff4213"
DEPENDS = "trousers"
LICENSE = "CPL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=059e8cd6165cb4c31e351f2b69388fd9"

SRC_URI = "http://downloads.sourceforge.net/trousers/${PN}-${PV}.tar.gz \
           file://tpm-tools-extendpcr-tool.patch;patch=1 \
           file://tpm-tools-unsealdata-tool.patch;patch=1 \
           file://tpm-tools-standalone.patch;patch=1 \
           file://tpm-tools-quote.patch;patch=1 \
           file://tpm-tools-quote-standalone.patch;patch=1 \
           file://tpm-tools-sealdata-pcrval.patch;patch=1 \
           file://tpm-tools-no-unused-but-set-variable.patch;patch=1 \
           file://tpm-tools-passwd-stdin.patch;patch=1 \
"

inherit xenclient autotools-brokensep

PACKAGES =+ "${PN}-sa"
FILES_${PN}-sa = "/usr/bin/tpm_extendpcr_sa"
FILES_${PN}-sa += "/usr/bin/tpm_sealdata_sa"
FILES_${PN}-sa += "/usr/bin/tpm_unsealdata_sa"
FILES_${PN}-sa += "/usr/bin/tpm_quote_sa"

inherit autotools

do_install_append() {
    rm ${D}/usr/sbin/tpm_clear
}
