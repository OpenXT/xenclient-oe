DESCRIPTION = "Small script to load SELinux policy."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    file://selinux-load.sh \
"

S = "${UNPACKDIR}"

inherit allarch

do_install() {
    install -d ${D}/sbin
    install -m 0755 ${UNPACKDIR}/selinux-load.sh ${D}/sbin
}
