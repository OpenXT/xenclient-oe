DESCRIPTION = "Small binary to load SELinux policy."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
PROVIDES = "selinux-load"

SRC_URI = " \
    file://selinux-load.sh \
    "

S = "${WORKDIR}"

inherit xenclient

do_install() {
	     install -d ${D}/sbin
             install -m 0755 ${WORKDIR}/selinux-load.sh ${D}/sbin
}
