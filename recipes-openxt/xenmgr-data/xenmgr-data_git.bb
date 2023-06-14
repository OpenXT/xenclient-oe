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
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "dojosdk-native"

PV = "0+git${SRCPV}"

SRCREV = "591c934bdffa0590504f37bc4f96159b016aff8f"
SRC_URI = "git://github.com/OpenXT/toolstack-data.git;protocol=https"

S = "${WORKDIR}/git"

export STAGING_IDLDATADIR
export STAGING_RPCGENDATADIR_NATIVE

inherit xc-rpcgen

do_configure[noexec] = "1"

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake DESTDIR=${D} install
}

FILES_${PN} = " \
    /usr/lib/xui \
"
