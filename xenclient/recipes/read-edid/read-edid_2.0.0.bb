SRC_URI[md5sum] = "586e7fa1167773b27f4e505edc93274b"
SRC_URI[sha256sum] = "246ec14ec509e09ac26fe6862b120481b2cc881e2f142ba40886d6eec15e77e8"
SECTION = "utils"
DESCRIPTION = "Read and parse the EDID of a monitor"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d0bf70fa1ef81fe4741ec0e6231dadfd"
PR = "r0"
DEPENDS = "libx86"

SRC_URI = "http://polypux.org/projects/read-edid/read-edid-${PV}.tar.gz"

inherit autotools
