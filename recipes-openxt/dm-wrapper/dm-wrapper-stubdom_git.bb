require dm-wrapper.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/dm-wrapper:"

SRCREV = "${AUTOREV}"

EXTRA_OECONF += "--disable-syslog"
