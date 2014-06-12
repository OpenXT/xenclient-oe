inherit xenclient
inherit xenclient-pq

require recipes/linux/linux.inc

DEPENDS += "bc-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
KERNEL_IMAGETYPE = "bzImage"

SRC_URI = "${OPENXT_MIRROR}/linux-3.11.10.4.tar.gz;name=kernel \
           ${OPENXT_GIT_MIRROR}/linux-3.11-pq.git;protocol=git;tag=${OPENXT_TAG} \
           file://defconfig"

S = "${WORKDIR}/linux-3.11.10.4"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
SRC_URI[kernel.md5sum] = "de35143a3d9bc37c87a13c2d3760e522"
SRC_URI[kernel.sha256sum] = "2aa4a14a022a7ad92db81888b0a4dde9b0d713c07da9d1e1e07c8152df0d1cf5"


