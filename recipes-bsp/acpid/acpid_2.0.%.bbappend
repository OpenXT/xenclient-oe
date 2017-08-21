FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://powerbtn \
    file://ac \
    file://ac_actions \
"

do_install_append () {
    install -m 644 ${WORKDIR}/powerbtn ${D}${sysconfdir}/acpi/events
    install -m 644 ${WORKDIR}/ac ${D}${sysconfdir}/acpi/events

    install -m 755 ${WORKDIR}/ac_actions ${D}${sysconfdir}/acpi/ac 

    install -d ${D}${sysconfdir}/acpi/actions
}
