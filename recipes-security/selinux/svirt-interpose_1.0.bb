DESCRIPTION = "SELinux qemu-dm interposer"
DEPENDS = "libselinux xen-tools"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM="file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://svirt-interpose.c"

S = "${UNPACKDIR}"

LDFLAGS += "-lxenstore -lselinux"

ASNEEDED = ""

do_compile() {
	     oe_runmake svirt-interpose
	     # ${STRIP} svirt-interpose
}

do_install() {
	     install -d ${D}${sbindir}
	     install -m 0755 ${UNPACKDIR}/svirt-interpose ${D}${sbindir}
}

DEBUG_BUILD = "1"
