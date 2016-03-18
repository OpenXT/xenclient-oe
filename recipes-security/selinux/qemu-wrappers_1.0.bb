DEPENDS = "libselinux"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
SRC_URI = "file://qemu-dm_alt.c	\
           file://Makefile      \
           file://qemu-dm-wrapper_alt"

S = "${WORKDIR}"
FILES_${PN} += " ${datadir}/xenclient/qemu-dm-wrapper_alt	\
                 ${libdir}/xen/bin/qemu-dm_alt "
FILES_${PN}-dbg += "${libdir}/xen/bin/.debug/*"

ASNEEDED = ""

inherit autotools-brokensep

do_install_append() {
        install -m 755 -d ${D}${datadir}/xenclient/
        install -m 755 ${WORKDIR}/qemu-dm-wrapper_alt ${D}${datadir}/xenclient/qemu-dm-wrapper_alt
}
