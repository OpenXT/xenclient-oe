require libgcrypt.inc

PR = "r1"

SRC_URI[md5sum] = "78f8f8bec4580f75b25816f7896d0389"
SRC_URI[sha256sum] = "cc98b1f64c9ae3b0185099cef4ac6c436a849095edf87f34157f0bb10e187990"

PRINC = "2"
# disable capabilities as gcrypt tries to drop privileges and this causes issues with cryptsetup:
# http://code.google.com/p/cryptsetup/issues/detail?id=47
EXTRA_OECONF += "--with-capabilities=no"
DEPENDS = "libgpg-error"
