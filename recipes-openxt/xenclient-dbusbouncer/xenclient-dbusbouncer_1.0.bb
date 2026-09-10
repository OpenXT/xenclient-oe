DESCRIPTION = "XenClient DBUS socket connections dom0-uivm"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
DEPENDS = "libargo xen-tools"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://dbusbouncer.c \
	   file://dbusbouncer.initscript \
"

INITSCRIPT_NAME = "dbusbouncer"
INITSCRIPT_PARAMS = "defaults 29 71"

S = "${UNPACKDIR}"

inherit update-rc.d

do_compile() {
	export LDLIBS="-largo -lxenstore"
	oe_runmake dbusbouncer
	# ${STRIP} dbusbouncer
}

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${UNPACKDIR}/dbusbouncer ${D}${sbindir}
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${UNPACKDIR}/dbusbouncer.initscript ${D}${sysconfdir}/init.d/dbusbouncer
}

DEBUG_BUILD = "1"
