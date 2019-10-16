DESCRIPTION = "Small binary to load SELinux policy."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PROVIDES = "selinux-load"

SRC_URI = " \
    file://selinux-load.sh \
    "

S = "${WORKDIR}"

ASNEEDED = ""

do_install() {
	     install -d ${D}/sbin
             install -m 0755 ${WORKDIR}/selinux-load.sh ${D}/sbin
}
