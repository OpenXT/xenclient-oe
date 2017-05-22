FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"
SRC_URI += " \
    file://CVE-2016-3189.patch \
"
