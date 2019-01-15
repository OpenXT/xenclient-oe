DESCRIPTION = "XenClient language sync daemon"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
RDEPENDS_${PN} += "dbus"

SRC_URI = "file://language-sync \
           file://language-sync.initscript"

INITSCRIPT_NAME = "language-sync"
INITSCRIPT_PARAMS = "defaults 80"

S = "${WORKDIR}"

inherit update-rc.d

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/language-sync ${D}${sbindir}

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/language-sync.initscript \
            ${D}${sysconfdir}/init.d/language-sync
}
