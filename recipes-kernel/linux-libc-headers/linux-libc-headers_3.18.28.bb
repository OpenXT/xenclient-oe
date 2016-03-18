require linux-libc-headers.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

SRC_URI = "https://www.kernel.org/pub/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.gz;name=kernel"

SRC_URI[kernel.md5sum] = "ec798ecdf07065b68ce6a98f47818698"
SRC_URI[kernel.sha256sum] = "b7a1fb79d55127cc75c7aaa6a5aca47d4b883e99f3eb93e7abe865d68803a60a"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

S = "${WORKDIR}/linux-${PV}"

PR = "1"
