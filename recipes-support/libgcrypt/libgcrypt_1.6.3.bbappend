PR .= ".1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

#SRC_URI += " \
#            file://libgcrypt-md5-checkpoint.patch \
#           "

CFLAGS_append = " -Wno-implicit-function-declaration "

# disable capabilities as gcrypt tries to drop privileges and this causes issues with cryptsetup:
# http://code.google.com/p/cryptsetup/issues/detail?id=47
EXTRA_OECONF += "--with-capabilities=no"
DEPENDS = "libgpg-error"
