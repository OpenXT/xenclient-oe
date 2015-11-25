DESCRIPTION = "read-edid elucidates various very useful informations from a conforming PnP monitor"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=292c42e2aedc4af636636bf7af0e2b26"

SRC_URI = "http://polypux.org/projects/read-edid/read-edid-${PV}.tar.gz "

SRC_URI[md5sum] = "016546e438bf6c98739ff74061df9854"
SRC_URI[sha256sum] = "c7c6d8440f5b90f98e276829271ccea5b2ff5a3413df8a0f87ec09f834af186f"

inherit pkgconfig cmake

# Don't build the classic VBE interface at all
EXTRA_OECMAKE = " -DCLASSICBUILD=OFF "

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/parse-edid/parse-edid ${D}${bindir}
    install -m 0755 ${B}/get-edid/get-edid ${D}${bindir}
}

