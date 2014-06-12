DESCRIPTION = "SELinux qemu-dm interposer"
DEPENDS = "libselinux xen-tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://${TOPDIR}/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "file://svirt-interpose.c"

S = "${WORKDIR}"

inherit xenclient

LDFLAGS += "-lxenstore -lselinux"

do_compile() {
	     oe_runmake svirt-interpose
	     # ${STRIP} svirt-interpose
}

do_install() {
	     install -d ${D}${sbindir}
	     install -m 0755 ${WORKDIR}/svirt-interpose ${D}${sbindir}
}

DEBUG_BUILD = "1"
