inherit gettext
SRC_URI[md5sum] = "ee1706b69bb76cc6d011757ea194f683"
SRC_URI[sha256sum] = "1fcddc2f2af31165f2227de294a948098ac756f42405ac3f478f2e6f8393b798"
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
