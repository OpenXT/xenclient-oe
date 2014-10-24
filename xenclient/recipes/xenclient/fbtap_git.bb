DESCRIPTION = "device which can allocate memory and can be abused e. g. to provide a surface for surfman's splash screen"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"

export MODULE_MAKE_FLAGS

PV = "0+git${SRCPV}"

SRCREV = "14a85aa26841b47dae23a44f74671a467cbab2be"
SRC_URI = "git://github.com/openxt/fbtap.git;protocol=https"

S = "${WORKDIR}/git"
MAKE_TARGETS += "modules"
FILES_${PN}-dev = " /usr/include "

do_install_headers() {
        install -m 0755 -d ${D}/usr/include
	install -m 644 fbtap.h ${D}/usr/include/fbtap.h
}

addtask install_headers after do_install before do_package do_populate_sysroot

inherit module-compat
inherit xenclient

