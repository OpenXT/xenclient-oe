DESCRIPTION = "XenClient shutdown script for displaying shutdown image"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = " \
    file://shutdown-screen.initscript \
"

inherit update-rc.d

INITSCRIPT_NAME = "shutdown-screen"
INITSCRIPT_PARAMS = "stop 15 0 6 ."

do_configure_append() {
    :
}

do_compile() {
    :
}

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/shutdown-screen.initscript ${D}${sysconfdir}/init.d/shutdown-screen
}
