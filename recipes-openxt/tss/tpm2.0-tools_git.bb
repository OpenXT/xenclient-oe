SUMMARY = "Tools for TPM2."
DESCRIPTION = "tpm2.0-tools"
SECTION = "tpm"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=91b7c548d73ea16537799e8060cea819"
DEPENDS = "tpm2.0-tss openssl curl autoconf-archive pkgconfig"
SRC_URI = "git://github.com/01org/tpm2.0-tools.git;protocol=git;branch=master;name=tpm2.0-tools;destsuffix=tpm2.0-tools \
    file://tpm2-tools-lib-support.patch \
    file://tpm2-sealing-support.patch \
    file://tpm2-unsealing-support.patch \
    file://tpm2-extendpcr-support.patch \
    file://tpm2-fix-forward-seal.patch \
"

S = "${WORKDIR}/tpm2.0-tools"
# https://lists.yoctoproject.org/pipermail/yocto/2013-November/017042.html
SRCREV = "2.0.0"
PVBASE := "${PV}"
PV = "${PVBASE}.${SRCPV}"

EXTRA_OECONF = "--with-tcti-device --without-tcti-socket"

inherit autotools pkgconfig

do_configure_prepend () {
	# execute the bootstrap script
	cd ${S}
	ACLOCAL="aclocal --system-acdir=${STAGING_DATADIR}/aclocal" ./bootstrap
	cd -
	oe_runconf
}
