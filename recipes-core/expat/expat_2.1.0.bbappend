FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"
SRC_URI += " \
    file://expat-CVE-2015-1283.patch \
    file://CVE-2016-0718.patch \
"
