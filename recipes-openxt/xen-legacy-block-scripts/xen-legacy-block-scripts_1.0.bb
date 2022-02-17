SUMMARY = "OpenXT customized Xen hotplug script for legacy block devices."
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780"

SRC_URI = " \
    file://block \
    file://block-frontend \
    file://xen-block-backend.rules \
"

S = "${WORKDIR}"

inherit allarch

do_install() {
    install -m 0755 -d ${D}${sysconfdir}/udev
    install -m 0755 -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${S}/xen-block-backend.rules ${D}${sysconfdir}/udev/rules.d/xen-block-backend.rules

    install -m 0755 -d ${D}${sysconfdir}/xen/scripts
    install -m 0755 ${S}/block ${D}${sysconfdir}/xen/scripts/block
    install -m 0755 ${S}/block-frontend ${D}${sysconfdir}/xen/scripts/block-frontend
}

RCONFLICTS_${PN} += "xen-block-scripts"
