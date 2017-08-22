FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"
SRC_URI += " \
    file://vhd.patch \
    file://autofix-sb-future-timestamps.patch \
"
DEPENDS += "libbudgetvhd"
