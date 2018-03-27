FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"
SRC_URI += " \
     file://libselinux-mount-procfs-before-check.patch;patch=1 \
"
