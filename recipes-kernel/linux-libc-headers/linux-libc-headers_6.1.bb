KORG_ARCHIVE_COMPRESSION = "xz"

# Use openembedded-core/meta/recipes-kernel/linux-libc-headers/linux-libc-headers.inc
require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

# Hack to get the -rc* libc-headers.
SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v6.x/linux-${PV}.tar.${KORG_ARCHIVE_COMPRESSION}"

S = "${WORKDIR}/linux-${PV}"

DEPENDS = "flex-native bison-native"
DEPENDS += "rsync-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI[md5sum] = "475320de08f16c9fa486fc4edfe98b30"
SRC_URI[sha256sum] = "2ca1f17051a430f6fed1196e4952717507171acfd97d96577212502703b25deb"
