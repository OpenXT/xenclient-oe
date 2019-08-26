PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

SRC_URI += "file://lock-cloexec.patch"
