require dm-agent.inc

SRCREV = "${OPENXT_TAG}"

EXTRA_OECONF += "--disable-syslog --disable-dmbus"
