inherit xenclient
inherit xenclient-pq

require recipes/linux/linux.inc

DEPENDS += "bc-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
KERNEL_IMAGETYPE = "bzImage"

SRCREV_FORMAT = "kernel_patchqueue"

PV = "3.11+git${SRCPV}"

SRCREV_patchqueue = "26a217dba072cbfd37acfda38fd33402adb8e9ff"

SRC_URI = "http://mirror.openxt.org/linux-3.11.10.4.tar.gz;name=kernel \
           git://github.com/openxt/linux-3.11-pq.git;protocol=https;destsuffix=patchqueue;name=patchqueue \
           file://defconfig"

SRC_URI[kernel.md5sum] = "de35143a3d9bc37c87a13c2d3760e522"
SRC_URI[kernel.sha256sum] = "2aa4a14a022a7ad92db81888b0a4dde9b0d713c07da9d1e1e07c8152df0d1cf5"

S = "${WORKDIR}/linux-3.11.10.4"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
