DESCRIPTION = "XenClient keyboard list"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
DEPENDS = "xkeyboard-config"

SRC_URI = "file://supported-keyboards \
           file://generate-keyboard-list"

FILES_${PN} = "${datadir}/xenclient/keyboards"

S = "${WORKDIR}/src"

inherit xenclient

do_compile() {
    perl ${WORKDIR}/generate-keyboard-list \
        ${STAGING_DATADIR}/X11/xkb/rules/evdev.lst \
        ${WORKDIR}/supported-keyboards > keyboards
}

do_install() {
    install -d ${D}${datadir}/xenclient
    install -m 0644 ${S}/keyboards ${D}${datadir}/xenclient/
}
