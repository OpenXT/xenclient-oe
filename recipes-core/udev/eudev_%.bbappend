FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " \
    file://05-db.rules \
"

do_install_append () {
    install -d ${D}/etc
    install -d ${D}/etc/udev
    install -d ${D}/etc/udev/rules.d
    install -m 0644 ${WORKDIR}/05-db.rules ${D}/etc/udev/rules.d/05-db.rules
}
