DESCRIPTION = "Console keymaps for XenClient"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
DEPENDS = "console-setup-ckbcomp-native \
           xkeyboard-config \
           xenclient-keyboard-list"

FILES_${PN} = "${datadir}/keymaps"

S = "${WORKDIR}/src"

do_compile() {
    while read LINE ; do
        KEYBOARD=$(echo "${LINE}" | cut -d: -f1)
        LAYOUT=$(echo "${LINE}" | cut -d: -f3)
        VARIANT=$(echo "${LINE}" | cut -d: -f4)

        ckbcomp -I${STAGING_DATADIR}/X11/xkb \
                "${LAYOUT}" "${VARIANT}" | gzip > "${KEYBOARD}.gz"
    done < ${STAGING_DATADIR}/xenclient/keyboards
}

do_install() {
    install -d ${D}${datadir}/keymaps
    install -m 0644 ${S}/*.gz ${D}${datadir}/keymaps
}
