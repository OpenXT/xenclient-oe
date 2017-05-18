SUMMARY = "Tools for TPM2."
DESCRIPTION = "tpm2.0-tools"
SECTION = "tpm"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=91b7c548d73ea16537799e8060cea819"
DEPENDS = "tpm2.0-tss openssl curl autoconf-archive pkgconfig"
SRC_URI = "git://github.com/01org/tpm2.0-tools.git;protocol=git;branch=master;name=tpm2.0-tools;destsuffix=tpm2.0-tools \
    file://0001-tpm2-tools-lib-support.patch \
    file://0002-tpm2-sealing-support.patch \
    file://0003-tpm2-unsealing-support.patch \
    file://0004-tpm2-extendpcr-support.patch \
"

S = "${WORKDIR}/tpm2.0-tools"
# https://lists.yoctoproject.org/pipermail/yocto/2013-November/017042.html
SRCREV = "2.0.0"
PVBASE := "${PV}"
PV = "${PVBASE}.${SRCPV}"

EXTRA_OECONF = "--with-tcti-device --without-tcti-socket"

inherit autotools pkgconfig

do_configure_prepend () {
	bbnote "test"
	# execute the bootstrap script
	currentdir=$(pwd)
	cd ${S}
	ACLOCAL="aclocal --system-acdir=${STAGING_DATADIR}/aclocal" ./bootstrap
	cd ${currentdir}
	oe_runconf
}
