require dm-agent.inc

SRCREV = "b22c4de4afeda3bfb238331f9e751ae95df2cebd"

SRC_URI += "file://dm-agent.initscript"

inherit update-rc.d

INITSCRIPT_NAME = "dm-agent"
INITSCRIPT_PARAMS = "defaults 72"

do_install_append() {
    install -m 0755 -d ${D}/${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/dm-agent.initscript ${D}/${sysconfdir}/init.d/dm-agent
}
