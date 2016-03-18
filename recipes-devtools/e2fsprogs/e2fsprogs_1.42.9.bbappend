PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
DEPENDS += "libbudgetvhd"
SRC_URI += "file://vhd.patch \
    file://autofix-sb-future-timestamps.patch \
    file://fix-infinite-recursion.patch \
"

do_configure_append() {
    # Not sure why this was needed before.  Causes an error now.
    #autoconf
}

