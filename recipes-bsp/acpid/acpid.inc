SUMMARY = "A daemon for delivering ACPI events"
HOMEPAGE = "http://sourceforge.net/projects/acpid2/"
BUGTRACKER = "http://sourceforge.net/p/acpid2/tickets/" 
SECTION = "base"
LICENSE = "GPLv2+"

SRC_URI = "${SOURCEFORGE_MIRROR}/acpid2/acpid-${PV}.tar.xz \
           file://init \
           file://powerbtn \
           file://ac \
           file://ac_actions \
          "

inherit autotools update-rc.d

INITSCRIPT_NAME = "acpid"
INITSCRIPT_PARAMS = "defaults"

do_install_append () {
    install -d ${D}${sysconfdir}/init.d
    sed -e 's,/usr/sbin,${sbindir},g' ${WORKDIR}/init > ${D}${sysconfdir}/init.d/acpid
    chmod 755 ${D}${sysconfdir}/init.d/acpid

    install -d ${D}${sysconfdir}/acpi
    install -d ${D}${sysconfdir}/acpi/events
    install -m 644 ${WORKDIR}/powerbtn ${D}${sysconfdir}/acpi/events
    install -m 644 ${WORKDIR}/ac ${D}${sysconfdir}/acpi/events

    install -m 755 ${WORKDIR}/ac_actions ${D}${sysconfdir}/acpi/ac 

    install -d ${D}${sysconfdir}/acpi/actions
}
