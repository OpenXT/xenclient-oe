require dm-agent.inc

SRCREV = "${AUTOREV}"

inherit update-rc.d

INITSCRIPT_NAME = "dm-agent"
INITSCRIPT_PARAMS = "defaults 72"

do_install_append() {
    install -m 0755 -d ${D}/${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/dm-agent.initscript ${D}/${sysconfdir}/init.d/dm-agent
}
