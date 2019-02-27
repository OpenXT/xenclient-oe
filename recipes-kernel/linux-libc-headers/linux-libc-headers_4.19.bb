KORG_ARCHIVE_COMPRESSION = "xz"

# Use openembedded-core/meta/recipes-kernel/linux-libc-headers/linux-libc-headers.inc
require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

# Hack to get the -rc* libc-headers.
SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v${HEADER_FETCH_VER}/linux-${PV}.tar.${KORG_ARCHIVE_COMPRESSION}"

S = "${WORKDIR}/linux-${PV}"

DEPENDS = "flex-native bison-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRC_URI[md5sum] = "740a90cf810c2105df8ee12e5d0bb900"
SRC_URI[sha256sum] = "0c68f5655528aed4f99dae71a5b259edc93239fa899e2df79c055275c21749a1"
