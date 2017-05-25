FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += " \
    file://CVE-2017-6965.patch \
    file://CVE-2017-6966.patch \
"
