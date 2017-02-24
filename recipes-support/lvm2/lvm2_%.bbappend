FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://0004-tweak-MODPROBE_CMD-for-cross-compile.patch \
"

CACHED_CONFIGUREVARS += "MODPROBE_CMD=${base_sbindir}/modprobe"
