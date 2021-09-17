KORG_ARCHIVE_COMPRESSION = "xz"

# Use openembedded-core/meta/recipes-kernel/linux-libc-headers/linux-libc-headers.inc
require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

# Hack to get the -rc* libc-headers.
SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v${HEADER_FETCH_VER}/linux-${PV}.tar.${KORG_ARCHIVE_COMPRESSION}"

S = "${WORKDIR}/linux-${PV}"

DEPENDS = "flex-native bison-native"
DEPENDS += "rsync-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI[md5sum] = "753adc474bf799d569dec4f165ed92c3"
SRC_URI[sha256sum] = "dcdf99e43e98330d925016985bfbc7b83c66d367b714b2de0cbbfcbf83d8ca43"
