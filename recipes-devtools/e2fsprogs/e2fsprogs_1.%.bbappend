PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BP}:"
SRC_URI += " \
    file://vhd.patch \
    file://autofix-sb-future-timestamps.patch \
    file://fix-infinite-recursion.patch \
"

DEPENDS += "libbudgetvhd"
