SUMMARY = "Tools for TPM2."
DESCRIPTION = "tpm2-tools"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://docs/LICENSE;md5=a846608d090aa64494c45fc147cc12e3"
SECTION = "tpm"

DEPENDS = "tpm2-abrmd tpm2-tss openssl curl autoconf-archive"

SRC_URI = "https://github.com/tpm2-software/${BPN}/releases/download/${PV}/${BPN}-${PV}.tar.gz"

SRC_URI[sha256sum] = "3810d36b5079256f4f2f7ce552e22213d43b1031c131538df8a2dbc3c570983a"

inherit autotools pkgconfig bash-completion

# need tss-esys
RDEPENDS:${PN} = "libtss2 tpm2-abrmd"
