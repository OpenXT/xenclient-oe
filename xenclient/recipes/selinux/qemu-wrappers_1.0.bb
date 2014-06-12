DEPENDS = "libselinux"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"
SRC_URI = "file://qemu-dm_alt.c	\
           file://Makefile      \
           file://qemu-dm-wrapper_alt"

S = "${WORKDIR}"
FILES_${PN} += " /usr/share/xenclient/qemu-dm-wrapper_alt	\
                 /usr/lib/xen/bin/qemu-dm_alt "

inherit autotools

do_install_append() {
        install -m 755 -d ${D}/usr/share/xenclient/
        install -m 755 ${WORKDIR}/qemu-dm-wrapper_alt ${D}/usr/share/xenclient/qemu-dm-wrapper_alt
}
