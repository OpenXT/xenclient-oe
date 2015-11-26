require linux-libc-headers.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

SRC_URI = "https://www.kernel.org/pub/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.gz;name=kernel"

SRC_URI[kernel.md5sum] = "f8adf8afc4de69663b53ae54919e01d4"
SRC_URI[kernel.sha256sum] = "fd725f60fd9080efd2c65302cf74dae1627d973a301ca60e75b49cc40568be8b"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

S = "${WORKDIR}/linux-${PV}"

PR = "1"
