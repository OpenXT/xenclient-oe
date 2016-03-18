PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://xenclient-4.3-support.patch"

EXTRA_OECONF = "--enable-tls --enable-xen"
