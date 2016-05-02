PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://openxt-xen-4.3.patch"

EXTRA_OECONF = "--enable-tls"
