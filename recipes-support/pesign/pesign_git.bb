SUMMARY = "pesign"
DESCRIPTION = "Signing tool for PE-COFF binaries, \
hopefully at least vaguely compliant with \
the PE and Authenticode specifications."
HOMEPAGE = "https://github.com/rhboot/pesign"
SECTION = "devtools"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS += " \
    util-linux \
    efivar nss nspr popt \
"

PV = "git${SRCPV}"

SRCREV = "be25a1be3d1b71bd747065f2b03c5a97e7a4ba20"

SRC_URI = "git://github.com/rhboot/pesign \
    file://0001-Disable-warning.patch \
    file://0002-Init-error.patch \
    "

S = "${WORKDIR}/git"

inherit pkgconfig

COMPATIBLE_HOST = 'i686-oe-linux|(x86_64.*).*-linux|aarch64.*-linux'

EXTRA_OEMAKE += "\
    CROSS_COMPILE=\"\" \
    C_INCLUDE_PATH=${STAGING_INCDIR}/efivar \
"

do_install() {
    install -d ${D}${bindir}
    install "${B}/src/pesign" ${D}${bindir}
}
