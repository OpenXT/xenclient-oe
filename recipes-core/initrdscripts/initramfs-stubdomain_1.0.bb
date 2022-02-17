DESCRIPTION = "Stubdomain initscript."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "xenclient-stubdomain"

SRC_URI = " \
    file://init.sh \
"

inherit allarch

do_install() {
    install -m 0755 ${WORKDIR}/init.sh ${D}/init
}

FILES_${PN} = " \
    /init \
"
