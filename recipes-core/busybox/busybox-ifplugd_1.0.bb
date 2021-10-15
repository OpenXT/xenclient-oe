DESCRIPTION = "ifplugd initscript and conf for busybox"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    file://ifplugd \
    file://ifplugd.action \
    file://ifplugd.conf \
"

inherit update-rc.d allarch

INITSCRIPT_NAME = "ifplugd"
INITSCRIPT_PARAMS = "defaults 30"

CONFFILES_${PN} = "${sysconfdir}/ifplugd/ifplugd.conf"

do_install_append() {
    install -d ${D}${sysconfdir}/ifplugd
    install -m 0644 ${WORKDIR}/ifplugd.conf ${D}${sysconfdir}/ifplugd/
    install -m 0755 ${WORKDIR}/ifplugd.action ${D}${sysconfdir}/ifplugd/

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/ifplugd ${D}${sysconfdir}/init.d/
}

RDEPENDS_${PN} += " \
    busybox \
"
