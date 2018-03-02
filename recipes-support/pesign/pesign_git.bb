SUMMARY = "pesign"
DESCRIPTION = "Signing tool for PE-COFF binaries, \
hopefully at least vaguely compliant with \
the PE and Authenticode specifications."
HOMEPAGE = "https://github.com/rhboot/pesign"
SECTION = "devtools"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS += "\
    efivar nss nspr popt \
"

PV = "git${SRCPV}"

SRCREV = "${AUTOREV}"

SRC_URI = "git://github.com/rhboot/pesign \
    file://0001-Disable-warning.patch \
    file://0002-Init-error.patch \
    "

S = "${WORKDIR}/git"

COMPATIBLE_HOST = 'i686-oe-linux|(x86_64.*).*-linux|aarch64.*-linux'

EXTRA_OEMAKE += "\
    CROSS_COMPILE=\"\" \
    C_INCLUDE_PATH=${STAGING_INCDIR}/efivar \
"

do_install() {
    install -d ${D}/usr/bin
    install "${B}/src/pesign" ${D}/usr/bin
}

FILES_${PN} = "/usr/bin/pesign"
