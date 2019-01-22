SUMMARY = "Tools for TPM2."
DESCRIPTION = "tpm2-tools"
SECTION = "tpm"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=91b7c548d73ea16537799e8060cea819"
DEPENDS = "tpm2-tss openssl curl autoconf-archive pkgconfig libgcrypt"

SRCREV = "74ba065e5914bc5d713ca3709d62a5751b097369"
SRC_URI = "git://github.com/01org/tpm2-tools.git;protocol=git;branch=3.X \
    file://tpm2-tools-lib-support.patch \
    file://tpm2-sealing-support.patch \
    file://tpm2-unsealing-support.patch \
    file://tpm2-extendpcr-support.patch \
"

S = "${WORKDIR}/git"
# https://lists.yoctoproject.org/pipermail/yocto/2013-November/017042.html

inherit autotools pkgconfig

do_configure_prepend () {
	# execute the bootstrap script
	cd ${S}
	ACLOCAL="aclocal --system-acdir=${STAGING_DATADIR}/aclocal" ./bootstrap
	cd -
	oe_runconf
}
