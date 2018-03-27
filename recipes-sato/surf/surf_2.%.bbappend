SRC_URI += " \
    file://surf-v4v.desktop \
"

do_install_append() {
    install -m 0755 -d ${D}${datadir}/applications
    install -m 0644 ${WORKDIR}/surf-v4v.desktop ${D}${datadir}/applications/surf-v4v.desktop
}
