FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"
SRC_URI += " \
    file://0001-xen-begin-syswrap-additions-post-4.5.patch \
    file://0002-xen-v4v-Add-the-v4v-chardev-ioctls.patch \
"
