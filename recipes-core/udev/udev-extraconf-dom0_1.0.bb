SUMMARY = "Extra machine specific configuration files."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://50-usb-powersave.rules \
"

inherit allarch

do_install() {
    install -d ${D}${sysconfdir}/udev/rules.d

    install -m 0644 ${WORKDIR}/50-usb-powersave.rules \
        ${D}${sysconfdir}/udev/rules.d/50-usb-powersave.rules
}

RDEPENDS_${PN} += "udev"
