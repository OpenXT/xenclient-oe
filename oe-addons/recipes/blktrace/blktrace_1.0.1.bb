DESCRIPTION = "Linux block IO profiler -- userspace tools"
LICENSE = "GPLv2"

DEPENDS = "libaio"

BLKTRACE_URI = "http://brick.kernel.dk/snaps"
SRC_URI = "${BLKTRACE_URI}/blktrace-${PV}.tar.bz2"

do_compile() {
    oe_runmake MAKEFLAGS="-e" CFLAGS="${CFLAGS} ${LDFLAGS}"
}

do_install() {
    oe_runmake DESTDIR="${D}" prefix=/usr install
}
