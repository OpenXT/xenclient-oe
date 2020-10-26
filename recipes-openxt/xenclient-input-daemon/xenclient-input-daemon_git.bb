DESCRIPTION = "Input daemon for XenClient"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xen libxcdbus udev openssl libevent libxcxenstore libdmbus libxenbackend xenfb2"

RDEPENDS_${PN} += "xenclient-keyboard-list libxcxenstore"

PV = "0+git${SRCPV}"

SRCREV = "799c4861b59b100a5fbd68d406821125de714d9d"
SRC_URI = "git://github.com/OpenXT/input.git \
	   file://input-daemon.initscript \
"

CFLAGS_append += " -Wno-unused-parameter -Wno-deprecated-declarations "

S = "${WORKDIR}/git"

ASNEEDED = ""

LDFLAGS += "-lm -lcrypto"

inherit autotools update-rc.d pkgconfig xc-rpcgen-c


INITSCRIPT_NAME = "xenclient-input"
INITSCRIPT_PARAMS = "defaults 75"

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/input-daemon.initscript ${D}${sysconfdir}/init.d/xenclient-input
}
