require recipes/qemu-dm/qemu-dm.inc

SRCREV_source = "${AUTOREV}"
SRCREV_patchqueue = "${AUTOREV}"

DEPENDS += " pciutils-static "

EXTRA_OECONF += " --static --disable-syslog "
