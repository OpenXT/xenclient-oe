SRC_URI[md5sum] = "a29ea5b5f0b9fa7ea64ec99c06266f47"
SRC_URI[sha256sum] = "6949de179a44ed99f57649181e7ddd0349f464fd488e0d4293953931355ef296"
DESCRIPTION = "Convert XKB keymap to console keymap"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYRIGHT;md5=e79b11599ed4d3eec05286ba3eaedd62"
RDEPENDS_${PN} = "perl-native"

# Don't bother building the whole package. We only want the ckbcomp script.

SRC_URI = "${DEBIAN_MIRROR}/main/c/console-setup/console-setup_${PV}.tar.gz"

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
