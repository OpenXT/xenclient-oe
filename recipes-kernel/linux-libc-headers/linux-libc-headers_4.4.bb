require linux-libc-headers.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.xz;name=kernel"

SRC_URI[kernel.md5sum] = "9a78fa2eb6c68ca5a40ed5af08142599"
SRC_URI[kernel.sha256sum] = "401d7c8fef594999a460d10c72c5a94e9c2e1022f16795ec51746b0d165418b2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

S = "${WORKDIR}/linux-${PV}"

PR = "1"
