FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://root.bashrc"

FILES_${PN} += "/root/.bashrc"

do_install_append() {
    install -d -m 0755 ${D}/root
    install -m 0755 ${WORKDIR}/root.bashrc ${D}/root/.bashrc
}
