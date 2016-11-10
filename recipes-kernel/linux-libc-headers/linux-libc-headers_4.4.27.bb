require linux-libc-headers.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.gz;name=kernel"

SRC_URI[kernel.md5sum] = "3132b13f70447a85d70f58037db3a770"
SRC_URI[kernel.sha256sum] = "0f995462425258b7fc50c03398a8f961d13a7e1df7955e7889ad1edd89da1882"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

S = "${WORKDIR}/linux-${PV}"

PR = "1"
