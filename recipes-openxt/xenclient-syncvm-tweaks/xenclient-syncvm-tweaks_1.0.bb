DESCRIPTION = "Various syncvm tweaks"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://argo.modutils \
           file://volatiles \
           file://network-config.initscript"

PACKAGES = "${PN}"

RDEPENDS:${PN} += "dbd-tools-vm"

FILES:${PN} = "/"

inherit update-rc.d

INITSCRIPT_NAME = "network-config"
INITSCRIPT_PARAMS = "start 39 S ."

do_install () {
    install -d ${D}/etc/modutils
    install -m 644 ${UNPACKDIR}/argo.modutils ${D}/etc/modutils

    install -d ${D}/etc/default/volatiles
    install -m 644 ${UNPACKDIR}/volatiles ${D}/etc/default/volatiles/01_syncvm

    install -d ${D}/etc/init.d
    install -m 0755 ${UNPACKDIR}/network-config.initscript ${D}/etc/init.d/network-config
}
