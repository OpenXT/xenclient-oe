SUMMARY = "Surf is a lightweight web browser."
DESCRIPTION = "surf is a simple web browser based on WebKit2/GTK+. It is able \
to display websites and follow links. It supports the XEmbed protocol which \
makes it possible to embed it in another application. Furthermore, one can \
point surf to another URI by setting its XProperties."
HOMEPAGE = "https://surf.suckless.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b57e7f7720307a02d5a6598b00fe3afa"

DEPENDS = " \
    webkitgtk \
"

SRC_URI = " \
    http://dl.suckless.org/surf/surf-${PV}.tar.gz \
    file://config.mk \
"
SRC_URI[md5sum] = "11713901fa83c536f3ddfacfc28c3acc"
SRC_URI[sha256sum] = "faee4c7a62c38fc9791eff1ad06787c3c9b2b79f338806827f5152a7bc54951d"

inherit pkgconfig

# Squash the config.mk with one OE compliant.
do_configure_prepend() {
    cp ${WORKDIR}/config.mk ${S}/config.mk
}

do_install() {
    oe_runmake DESTDIR=${D} install
}
