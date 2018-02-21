DESCRIPTION = "Surf is a lightweight web browser."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=33ba16e67a5fa2edcbace4369802fedd"

DEPENDS = " \
    webkit-gtk \
    gtk+ \
    xserver-xorg \
"

inherit autotools-brokensep pkgconfig

SRC_URI = "http://dl.suckless.org/surf/surf-${PV}.tar.gz \
           file://reload-on-sighup.patch \
           file://config.mk \
          "

SRC_URI[md5sum] = "45899435aeb5ce3af0a62909911b735f"
SRC_URI[sha256sum] = "95608546fb64d01c7a8153c356be0e284ebe120c3c596a94eb3f3ad47e1c494a"

FILES_${PN} += "/usr"
FILES_${PN}-dbg += "/usr/bin/.debug"

do_configure_prepend() {
     sed -i "s/~/\/tmp/g" ${S}/config.def.h
     cp ${WORKDIR}/config.mk ${S}/config.mk
}
