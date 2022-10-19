SUMMARY = "Utility pass an open file descriptor to QEMU"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
"

SRC_URI = " \
    git://github.com/QubesOS/qubes-vmm-xen-stubdom-linux/ \
"
SRCREV = "2a814bd1edaf549ef9252eb6747aa6137abf9831"

S = "${WORKDIR}/git"

do_configure() {
}

do_compile() {
    make -C helpers add-fd
}

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 helpers/add-fd ${D}${bindir}/
}
