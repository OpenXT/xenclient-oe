PR .= ".1"

#
# Ensure that we're using our own version of coreutils, rather than
# the host's coreutils, as we'll need to use ln --relative.
#
DEPENDS += "attr-native coreutils-native"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://libselinux-mount-procfs-before-check.patch;patch=1 \
    file://libselinux-only-mount-proc-if-necessary.patch;patch=1 \
    "
