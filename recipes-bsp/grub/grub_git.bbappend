PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-git:"

SRC_URI += " \
    file://remove-editing-and-shell.patch \
    file://no-multiboot-display-reset.patch \
    "

PACKAGECONFIG = "device-mapper"
