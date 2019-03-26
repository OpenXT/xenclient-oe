SRC_URI[md5sum] = "ac695c7cd7b615ce069e6623b55f07db"
SRC_URI[sha256sum] = "5798b1f39267f54aacbb374a826984ea6b3bf08b0ffedfc1e71516dfedcd5bf6"
DESCRIPTION = "Convert XKB keymap to console keymap"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYRIGHT;md5=49cab1cfd397b014807c5b2bcc63e04f"
RDEPENDS_${PN} = "perl-native"

# Don't bother building the whole package. We only want the ckbcomp script.

SRC_URI = "${DEBIAN_MIRROR}/main/c/console-setup/console-setup_${PV}.tar.xz"

S = "${WORKDIR}/console-setup-${PV}"

inherit native

do_compile() {
    :
}

do_install() {
    mkdir -p "${D}/${bindir}"
    install -m 755 Keyboard/ckbcomp "${D}/${bindir}"
}

#do_stage() {
#    mkdir -p ${STAGING_BINDIR}
#    install -m 755 Keyboard/ckbcomp ${STAGING_BINDIR}/
#}
