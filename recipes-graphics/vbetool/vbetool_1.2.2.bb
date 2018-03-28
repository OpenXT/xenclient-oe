DESCRIPTION = "vbetool uses lrmi in order to run code from the video BIOS. \
Currently, it is able to alter DPMS states, save/restore video card state and \
attempt to initialize the video card from scratch."
HOMEPAGE = "https://cgit.freedesktop.org/~airlied/vbetool"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=48a5edcd17b7ae645f03eef18cd5e540"
DEPENDS = "libx86-1 libpciaccess zlib"

SRC_URI = " \
    https://cgit.freedesktop.org/~airlied/${BPN}/snapshot/${BPN}-${PV}.tar.gz \
    file://no-x86-checks.patch \
"
SRC_URI[md5sum] = "cacc068e7b77f0be749a0d1ed1772228"
SRC_URI[sha256sum] = "845f6f7b0e819533251e651f82185abce0126afc6a623d82286ba9a3dbc8e423"

inherit autotools pkgconfig
