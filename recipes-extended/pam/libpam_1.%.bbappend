FILESEXTRAPATHS_prepend := "${THISDIR}/patches:${THISDIR}/libpam:"

SRC_URI += " \
    file://etc-config-passwd.patch \
"
