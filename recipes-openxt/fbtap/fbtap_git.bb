DESCRIPTION = "device which can allocate memory and can be abused e. g. to provide a surface for surfman's splash screen"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

SRC_URI = "git://github.com/OpenXT/fbtap.git"
SRCREV = "30fd6ec306b188030a2fa58cde29c3e7f129c908"

S = "${WORKDIR}/git"

inherit module
inherit module-signing

EXTRA_OEMAKE += "INSTALL_HDR_PATH=${D}${prefix}"
MODULES_INSTALL_TARGET += "headers_install"
