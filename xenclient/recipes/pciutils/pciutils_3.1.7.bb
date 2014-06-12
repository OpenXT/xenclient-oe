SRC_URI[md5sum] = "35222edb86b50a6641352c66fe829b2e"
SRC_URI[sha256sum] = "d8fe23b6966c1abf29b3b38b08b0cf33f731cd6e6a89d9b8d2b8d5e982c3f544"
require recipes/pciutils/pciutils.inc

SRC_URI += " file://pciutils-3.1.patch;patch=1 "
SRC_URI += " file://config.h "
SRC_URI += " file://config.mk "

do_configure() {
  cp ${WORKDIR}/config.mk ${WORKDIR}/config.h ${S}/lib
}
