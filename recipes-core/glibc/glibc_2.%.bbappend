FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += " \
    file://CVE-2017-1000366.patch \
    file://CVE-2019-9169.patch \
    file://CVE-2017-18269.patch \
    file://CVE-2018-11236.patch \
"
