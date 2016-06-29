require libressl.inc

# For target side versions of libressl enable support for OCF Linux driver
# if they are available.
DEPENDS += "cryptodev-linux"

CFLAG += "-DHAVE_CRYPTODEV -DUSE_CRYPTODEV_DIGESTS"

LIC_FILES_CHKSUM = "file://COPYING;md5=01f9bb4d275f5eeea905377bef3de622"

export DIRS = "crypto ssl apps"
export OE_LDFLAGS="${LDFLAGS}"

SRC_URI[md5sum] = "2310f6524d8cec7f32986b32497926a2"
SRC_URI[sha256sum] = "358a4779e6813bd06f07db0cf0f0fe531401ed0c6ed958973d404416c3d537fa"
