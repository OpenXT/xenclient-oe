KORG_ARCHIVE_COMPRESSION = "xz"

# Use openembedded-core/meta/recipes-kernel/linux-libc-headers/linux-libc-headers.inc
require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

# Hack to get the -rc* libc-headers.
SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v${HEADER_FETCH_VER}/linux-${PV}.tar.${KORG_ARCHIVE_COMPRESSION}"

S = "${WORKDIR}/linux-${PV}"

SRC_URI[md5sum] = "bacdb9ffdcd922aa069a5e1520160e24"
SRC_URI[sha256sum] = "f81d59477e90a130857ce18dc02f4fbe5725854911db1e7ba770c7cd350f96a7"
