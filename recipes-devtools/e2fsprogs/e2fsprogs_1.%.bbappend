FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"
SRC_URI += " \
    file://vhd.patch \
"
DEPENDS += "libbudgetvhd"
