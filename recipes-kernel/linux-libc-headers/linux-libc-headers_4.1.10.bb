require linux-libc-headers.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

SRC_URI = "https://www.kernel.org/pub/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.gz;name=kernel"

SRC_URI[kernel.md5sum] = "5d61c4f6423000989736330e305ecf4e"
SRC_URI[kernel.sha256sum] = "8b21dd7e0a5b55a6dc7b9bc081c6bf0f46c8288015817ce6e6a53d0ad7f535f5"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

S = "${WORKDIR}/linux-${PV}"

PR = "1"
