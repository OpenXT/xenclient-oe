DESCRIPTION = "LIBDRM is the cross-driver middleware which allows user-space applications (such as Mesa and 2D drivers) to communicate with the Kernel by the means of the DRI protocol"
HOMEPAGE = "https://01.org/linuxgraphics/community/libdrm"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://xf86drmMode.h;beginline=16;endline=32;md5=538ffe991bdeb5c8650b92022e54ea6d"
PR = "r0"
DEPENDS = "libpciaccess"

SRC_URI = "http://dri.freedesktop.org/libdrm/${PN}-${PV}.tar.gz;name=tarball    \
           file://libdrm-foreign.patch;patch=1                                  \
           "

SRC_URI[tarball.md5sum] = "50fde6cdaeb1e2b8b931a8b09bde5f72"
SRC_URI[tarball.sha256sum] = "8abc3c1f51fdc8fb7a0e5c989e2120ecaf186f9ab65ba1f5599a730dd3712046"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools

