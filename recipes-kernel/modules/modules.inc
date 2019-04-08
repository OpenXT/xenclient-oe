SUMMARY = "Kernel modules to load at boot time."
DESCRIPTION = "The /etc/modules file contains the names of kernel modules \
that are to be loaded at boot time, one per line. Arguments can be given in the \
same line as the module name. Lines beginning with a '#' are ignored."
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

SRC_URI = " \
    file://modules \
"

INC_PR = "r1"

inherit allarch

do_install() {
	install -d ${D}${sysconfdir}
	install ${WORKDIR}/modules ${D}${sysconfdir}
}

CONFFILES_${PN} += "${sysconfdir}/modules"
