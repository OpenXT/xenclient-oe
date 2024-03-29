SUMMARY = "OpenXT customized Xen hotplug script for vif/vwif in NDVM."
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780"

SRC_URI = " \
    file://vif \
    file://xen-vif-backend.rules \
"

S = "${WORKDIR}"

inherit allarch

do_install() {
    install -m 0755 -d ${D}${sysconfdir}/udev
    install -m 0755 -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${S}/xen-vif-backend.rules ${D}${sysconfdir}/udev/rules.d/xen-vif-backend.rules

    install -m 0755 -d ${D}${sysconfdir}/xen/scripts
    install -m 0755 ${S}/vif ${D}${sysconfdir}/xen/scripts/vif
}
