DESCRIPTION = "UID - User Interface Daemon"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
DEPENDS = " \
    dbus-native \
    ocaml-dbus \
    xen-ocaml-libs \
    xenclient-toolstack \
"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/uid.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

SRC_URI += " \
    file://uid_dbus.conf \
    file://uid.conf \
    file://uid.initscript \
"

S = "${WORKDIR}/git"

inherit update-rc.d ocaml findlib pkgconfig

INITSCRIPT_PACKAGES="${PN}"

INITSCRIPT_NAME_${PN} = "uid"
INITSCRIPT_PARAMS_${PN} = "defaults 81"

FILES_${PN} += " \
    ${bindir}/uid \
    ${sysconfdir}/init.d/uid \
    ${sysconfdir}/uid.conf \
    ${sysconfdir}/dbus-1/system.d/uid_dbus.conf \
"

PARALLEL_MAKE = ""
do_compile() {
    oe_runmake V=1 XEN_DIST_ROOT="${STAGING_DIR}" all
}

do_install() {
    oe_runmake DESTDIR=${D} V=1 install

    # No library.
    rm -rf ${D}${libdir}

    install -m 0755 -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/uid.conf ${D}${sysconfdir}/uid.conf

    install -m 0755 -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${WORKDIR}/uid_dbus.conf ${D}${sysconfdir}/dbus-1/system.d/uid_dbus.conf

    install -m 0755 -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/uid.initscript ${D}${sysconfdir}/init.d/uid
}
