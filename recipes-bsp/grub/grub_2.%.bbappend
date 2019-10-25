PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://remove-editing-and-shell.patch \
    file://no-multiboot-display-reset.patch \
"

PACKAGECONFIG_append = "device-mapper"

# PACKAGECONFIG seems to not append the RDEPENDS_${PN}-*... not sure why yet.
RDEPENDS_${PN}-editenv += "libdevmapper"
RDEPENDS_${PN}-common += "libdevmapper"
