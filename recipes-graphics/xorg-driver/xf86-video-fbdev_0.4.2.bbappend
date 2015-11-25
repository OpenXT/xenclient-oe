PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://fbdev-randr1.2.patch"
