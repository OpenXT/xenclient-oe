# Use OpenXT defeconfig.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:${THISDIR}/${BPN}:"
SRC_URI += " \
    file://CVE-2016-6301.patch \
"
