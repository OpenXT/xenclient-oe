FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"

SRC_URI += " \
    file://default-certs-dir.patch \
"
