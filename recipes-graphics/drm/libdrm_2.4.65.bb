DESCRIPTION = "LIBDRM is the cross-driver middleware which allows user-space applications (such as Mesa and 2D drivers) to communicate with the Kernel by the means of the DRI protocol"
HOMEPAGE = "https://01.org/linuxgraphics/community/libdrm"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://xf86drmMode.h;beginline=16;endline=32;md5=538ffe991bdeb5c8650b92022e54ea6d"
PR = "r0"
DEPENDS = "libpciaccess"

SRC_URI = "http://dri.freedesktop.org/libdrm/${PN}-${PV}.tar.gz;name=tarball \
           file://libdrm-foreign.patch;patch=1 \
           "

SRC_URI[tarball.md5sum] = "de91d20c354ca46ebbc134b9e34faa9e"
SRC_URI[tarball.sha256sum] = "b4382d6018464ba5bece2d7a4b87eb0eba24d891ff2ba29549be9511bd231c2f"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools

