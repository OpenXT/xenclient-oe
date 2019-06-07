SRC_URI += " \
    file://surf-argo.desktop \
"

do_install_append() {
    install -m 0755 -d ${D}${datadir}/applications
    install -m 0644 ${WORKDIR}/surf-argo.desktop ${D}${datadir}/applications/surf-argo.desktop
}
