FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"
SRC_URI += " \
     file://libselinux-mount-procfs-before-check.patch;patch=1 \
"

# We need to support stat on files >2GB in size.
CFLAGS += "-D_FILE_OFFSET_BITS=64"
