DESCRIPTION = "XenClient keyboard list"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
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
