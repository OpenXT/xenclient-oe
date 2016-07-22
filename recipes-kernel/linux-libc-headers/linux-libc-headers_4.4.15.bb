require linux-libc-headers.inc

PV_MAJOR = "${@"${PV}".split('.', 3)[0]}"

SRC_URI = "https://www.kernel.org/pub/linux/kernel/v${PV_MAJOR}.x/linux-${PV}.tar.gz;name=kernel"

SRC_URI[kernel.md5sum] = "9a0ed9ec1e6b379969cac4f3740bca9e"
SRC_URI[kernel.sha256sum] = "af21b6fe1a4a46602b49ddaeb139a2e23f0f4a7a50c1ba1fd532433b569e4872"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

S = "${WORKDIR}/linux-${PV}"

PR = "1"
