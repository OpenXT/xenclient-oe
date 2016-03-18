PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://fix-parallel-make.patch"

SRC_URI[archive.md5sum] = "79e5494d5b18d85cc82708417735713a"
SRC_URI[archive.sha256sum] = "30e572d3bac0f4c8ba0098dd63527a789cb85649bd4c8d5ffd8d1f34306e36d1"

B = "${S}"

GNOME_COMPRESS_TYPE = "gz"
