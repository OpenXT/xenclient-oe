require linux-libc-headers.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

SRC_URI = "https://www.kernel.org/pub/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.gz;name=kernel"

SRC_URI[kernel.md5sum] = "30cfa07084f3ef3709b350ed5f7f01b0"
SRC_URI[kernel.sha256sum] = "e05ea8886382e4a61945bb433d430d0e230cb6c0ce0c503303138d017350346a"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

S = "${WORKDIR}/linux-${PV}"

PR = "1"
