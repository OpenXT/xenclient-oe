DESCRIPTION = "DMI (Desktop Management Interface) table related utilities"
SECTION = "console/utils"
HOMEPAGE = "http://www.nongnu.org/dmidecode/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "files://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"
PR = "r1"

SRC_URI = "http://savannah.nongnu.org/download/dmidecode/${P}.tar.gz"

COMPATIBLE_HOST = "(i.86|x86_64).*-linux"

do_install() {
	oe_runmake DESTDIR="${D}" install
}

SRC_URI[md5sum] = "be7501ad0f844e875976b96106afaa3c"
SRC_URI[sha256sum] = "698d209ec81f88b2685e07943cd61e1ac125d8f4b3f3f22c777f318a56d94edf"
