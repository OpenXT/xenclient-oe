DESCRIPTION = "DMI (Desktop Management Interface) table related utilities"
SECTION = "console/utils"
HOMEPAGE = "http://www.nongnu.org/dmidecode/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "files://LICENSE;md5=393a5ca445f6965873eca0259a17f833"
PR = "r1"

SRC_URI = "http://savannah.nongnu.org/download/dmidecode/${P}.tar.bz2"

COMPATIBLE_HOST = "(i.86|x86_64).*-linux"

do_install() {
	oe_runmake DESTDIR="${D}" install
}

SRC_URI[md5sum] = "535487cc041f2db746587cf40a2059f0"
SRC_URI[sha256sum] = "6da5643479b3b591b71a91122a25b89b77920b66e1ad0a5162d3c75b50423b51"
