DESCRIPTION = "Linux Framebuffer PV driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://../COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0+git${SRCPV}"
SRC_URI = "git://github.com/OpenXT/xenfb2.git"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/linux"

inherit module
inherit module-signing

EXTRA_OEMAKE += "INSTALL_HDR_PATH=${D}${prefix}"
MODULES_INSTALL_TARGET += "headers_install"

KERNEL_MODULE_AUTOLOAD += "xenfb2"
