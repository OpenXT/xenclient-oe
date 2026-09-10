PR .= ".1"

FILESEXTRAPATHS:prepend := "${THISDIR}/patches:"

SRC_URI += "file://lock-cloexec.patch"
