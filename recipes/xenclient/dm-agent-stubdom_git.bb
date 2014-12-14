require dm-agent.inc

SRCREV = "${AUTOREV}"

EXTRA_OECONF += "--disable-syslog --disable-dmbus"
