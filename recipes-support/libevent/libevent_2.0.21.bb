require libevent.inc

SRC_URI = "https://github.com/downloads/libevent/libevent/${PN}-${PV}-stable.tar.gz;name=tarball"
SRC_URI[tarball.md5sum] = "b2405cc9ebf264aa47ff615d9de527a2"
SRC_URI[tarball.sha256sum] = "22a530a8a5ba1cb9c080cba033206b17dacd21437762155c6d30ee6469f574f5"

LIC_FILES_CHKSUM = "file://LICENSE;md5=45c5316ff684bcfe2f9f86d8b1279559"

PR = "r0"

S = "${WORKDIR}/${PN}-${PV}-stable"

inherit autotools

