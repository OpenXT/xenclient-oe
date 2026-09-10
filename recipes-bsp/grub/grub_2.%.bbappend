PR .= ".1"

FILESEXTRAPATHS:prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://remove-editing-and-shell.patch \
    file://no-multiboot-display-reset.patch \
"

PACKAGECONFIG:append = "device-mapper"

# PACKAGECONFIG seems to not append the RDEPENDS:${PN}-*... not sure why yet.
RDEPENDS:${PN}-editenv += "libdevmapper"
RDEPENDS:${PN}-common += "libdevmapper"
