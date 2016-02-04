require dm-agent.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/dm-agent:"

SRCREV = "${AUTOREV}"

EXTRA_OECONF += "--disable-syslog --disable-dmbus"
