DESCRIPTION = "LIBDRM is the cross-driver middleware which allows user-space applications (such as Mesa and 2D drivers) to communicate with the Kernel by the means of the DRI protocol"
HOMEPAGE = "https://01.org/linuxgraphics/community/libdrm"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://xf86drmMode.h;beginline=16;endline=32;md5=538ffe991bdeb5c8650b92022e54ea6d"
PR = "r0"
DEPENDS = "libpciaccess"

SRC_URI = "http://dri.freedesktop.org/libdrm/${PN}-${PV}.tar.gz;name=tarball \
           file://libdrm-userptr.patch;patch=1 \
           file://libdrm-foreign.patch;patch=1"

SRC_URI[tarball.md5sum] = "b454a43366eb386294f87a5cd16699e6"
SRC_URI[tarball.sha256sum] = "75dda05aa7717594d48f215d598525ffb7d4c60f60cc3fc2084672ca5d3ae039"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools

