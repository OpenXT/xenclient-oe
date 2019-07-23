SRC_URI[md5sum] = "aa522a2fc8f596fd71f51e581b7471dc"
SRC_URI[sha256sum] = "4dbe2c9559ff68a61315efe4de2cb932396ede8c4a1df4b46b6fb0846cc59d5b"
DESCRIPTION = "Convert XKB keymap to console keymap"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYRIGHT;md5=49cab1cfd397b014807c5b2bcc63e04f"
RDEPENDS_${PN} = "perl-native"

# Don't bother building the whole package. We only want the ckbcomp script.

SRC_URI = "${DEBIAN_MIRROR}/main/c/console-setup/console-setup_${PV}.tar.xz"

S = "${WORKDIR}/console-setup"

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
