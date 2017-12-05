DESCRIPTION = "UID - User Interface Daemon"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
DEPENDS = "ocaml-dbus xen-ocaml-libs xenclient-toolstack"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/uid.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

SRC_URI += "file://uid_dbus.conf \
	    file://uid.conf \
	    file://uid.initscript \
"

S = "${WORKDIR}/git"

inherit update-rc.d ocaml findlib

INITSCRIPT_PACKAGES="${PN}"

INITSCRIPT_NAME_${PN} = "uid"
INITSCRIPT_PARAMS_${PN} = "defaults 81"

FILES_${PN} += "/usr/bin/uid /etc/init.d/uid /etc/uid.conf /etc/dbus-1/system.d/uid_dbus.conf"

do_compile() {
	# hack
	touch ${STAGING_LIBDIR}/ocaml/ld.conf
	make V=1 XEN_DIST_ROOT="${STAGING_DIR}" TARGET_PREFIX="${TARGET_PREFIX}" STAGING_DIR="${STAGING_DIR}" STAGING_BINDIR_CROSS="${STAGING_BINDIR_CROSS}" STAGING_LIBDIR="${STAGING_LIBDIR}" STAGING_INCDIR="${STAGING_INCDIR}" all
}

do_install() {
	# No library.
	rm -rf ${D}${libdir}
	make DESTDIR=${D} V=1 install
	install -m 0755 -d ${D}/etc
	install -m 0644 ${WORKDIR}/uid.conf ${D}/etc
	install -m 0755 -d ${D}/etc/dbus-1/system.d
        install -m 0644 ${WORKDIR}/uid_dbus.conf ${D}/etc/dbus-1/system.d/
	install -m 0755 -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/uid.initscript ${D}${sysconfdir}/init.d/uid
}
