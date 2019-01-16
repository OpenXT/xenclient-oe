DESCRIPTION = "Dom0 policy file for rpc-broker"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://rpc-broker-dom0.rules\
"

FILES_${PN} += "/etc/rpc-broker.rules"

do_install() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/rpc-broker-dom0.rules ${D}${sysconfdir}/rpc-broker.rules
}

