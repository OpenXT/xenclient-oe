FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"


SRC_URI = "${SOURCEFORGE_MIRROR}/acpid2/acpid-${PV}.tar.xz \
           file://init \
           file://powerbtn \
           file://ac \
           file://ac_actions \
	   file://acpid.service \
          "


inherit autotools update-rc.d

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
