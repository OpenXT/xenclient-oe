DESCRIPTION = "Input daemon for XenClient"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "xenclient-idl dbus xen-tools libxcdbus udev xenclient-rpcgen-native openssl libevent libxcxenstore libdmbus libxenbackend xenfb2"

RDEPENDS_${PN} += "xenclient-keyboard-list libxcxenstore"

SRC_URI = "${OPENXT_GIT_MIRROR}/input.git;protocol=git;tag=${OPENXT_TAG} \
	   file://input-daemon.initscript \
"
EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR}"

S = "${WORKDIR}/git"

inherit autotools
inherit xenclient
inherit update-rc.d

INITSCRIPT_NAME = "xenclient-input"
INITSCRIPT_PARAMS = "defaults 75"

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/input-daemon.initscript ${D}${sysconfdir}/init.d/xenclient-input
}
