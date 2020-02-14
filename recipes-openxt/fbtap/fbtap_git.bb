DESCRIPTION = "device which can allocate memory and can be abused e. g. to provide a surface for surfman's splash screen"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

SRC_URI = "git://${OPENXT_GIT_MIRROR}/fbtap.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit module
inherit module-signing

EXTRA_OEMAKE += "INSTALL_HDR_PATH=${D}${prefix}"
MODULES_INSTALL_TARGET += "headers_install"
