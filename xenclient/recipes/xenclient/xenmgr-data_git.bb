# This recipe is a mess:
#
# 1) it uses precompiled binaries
# 2) it compies those binaries into the source tree
# 3) it can't run the compile step twice
# 4) the install step has unexpected dependancies on the source code
# 5) the output depends on the installed platform java, not on a java
#    interpreter managed by the build system
# 6) this recipe has the wrong name - it should match the new name of the repo
#
# why isn't the rpc generation part of the source's build makefile/script
# it's clearly not part of a configuration script.
#

DESCRIPTION = "XenClient xenmgr data"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "xenclient-rpcgen-native xenclient-idl dojosdk-native"

SRC_URI = "${OPENXT_GIT_MIRROR}/toolstack-data.git;protocol=git;tag=${OPENXT_TAG}"

S = "${WORKDIR}/git"
OUTPUT_DIR = "${S}/dist/script/services"
IDL_DIR = "${STAGING_DATADIR}/idl"

export IDL_DIR

inherit xenclient

do_configure() {
	:
}

do_compile() {
    make
}

do_install() {
    make DESTDIR=${D} install
}

FILES_${PN} = "/usr/lib/xui"
