DESCRIPTION = "Convert XKB keymap to console keymap"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYRIGHT;md5=49cab1cfd397b014807c5b2bcc63e04f"
RDEPENDS_${PN} = "perl-native"

# Don't bother building the whole package. We only want the ckbcomp script.

SRC_URI = "git://salsa.debian.org/installer-team/console-setup.git;protocol=https"
# The hash of the 1.191 tag:
SRCREV = "681544fa6481fb62b362f4fadeda5bbdf3389307"

S = "${WORKDIR}/git"

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
