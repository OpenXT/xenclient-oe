DESCRIPTION = "Various tweaks for XenClient Installer"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://status-report \
           file://60installer \
           file://prepare-hd-install \
           file://i915.conf \
           file://console-bell.initscript"
LICENSE = "Proprietary"
PR = "r6"

FILES_${PN} = "/"

inherit update-rc.d

INITSCRIPT_NAME = "console-bell"
INITSCRIPT_PARAMS = "start 90 S ."

do_install () {
    install -d ${D}/etc
    install -d ${D}/etc/udhcpc.d
    install -m 755 ${WORKDIR}/60installer ${D}/etc/udhcpc.d

    install -d ${D}/usr/bin
    install -m 0755 ${WORKDIR}/status-report ${D}/usr/bin/status-report
    install -m 0755 ${WORKDIR}/prepare-hd-install ${D}/usr/bin/prepare-hd-install

    install -d ${D}/etc/modprobe.d
    install -m 0644 ${WORKDIR}/i915.conf ${D}/etc/modprobe.d/i915.conf

    install -d ${D}/etc/init.d
    install -m 0755 ${WORKDIR}/console-bell.initscript ${D}/etc/init.d/console-bell
}
