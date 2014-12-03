DESCRIPTION = "Various syncvm tweaks"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://v4v.modutils \
           file://volatiles \
           file://network-config.initscript"

PACKAGES = "${PN}"

RDEPENDS_${PN} += "db-tools"

FILES_${PN} = "/"

inherit update-rc.d

INITSCRIPT_NAME = "network-config"
INITSCRIPT_PARAMS = "start 39 S ."

do_install () {
    install -d ${D}/etc/modutils
    install -m 644 ${WORKDIR}/v4v.modutils ${D}/etc/modutils

    install -d ${D}/etc/default/volatiles
    install -m 644 ${WORKDIR}/volatiles ${D}/etc/default/volatiles/01_syncvm

    install -d ${D}/etc/init.d
    install -m 0755 ${WORKDIR}/network-config.initscript ${D}/etc/init.d/network-config
}
