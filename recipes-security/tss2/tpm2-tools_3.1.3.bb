SUMMARY = "Tools for TPM2."
DESCRIPTION = "tpm2-tools"
SECTION = "tpm"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=91b7c548d73ea16537799e8060cea819"
DEPENDS = "tpm2-tss openssl curl autoconf-archive pkgconfig libgcrypt"

SRCREV = "74ba065e5914bc5d713ca3709d62a5751b097369"
SRC_URI = "git://github.com/01org/tpm2-tools.git;protocol=https;branch=3.X \
    file://tpm2-tools-lib-support.patch \
    file://tpm2-sealing-support.patch \
    file://tpm2-unsealing-support.patch \
    file://tpm2-extendpcr-support.patch \
    file://tpm2-nvlist-drop-ntoh.patch \
"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

PACKAGES =+ "tpm2-tools-initrd"
FILES_${PN}-initrd = " \
    ${bindir}/tpm2_pcrlist \
    ${bindir}/tpm2_extendpcr \
"
RDEPENDS_${PN} += "${PN}-initrd"
