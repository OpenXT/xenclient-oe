DESCRIPTION = "XenClient DBUS library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "dbus dbus-glib xenclient-idl xenclient-rpcgen-native libtool libevent"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/libxcdbus.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR}"

LICENSE="Proprietary"

S = "${WORKDIR}/git"

inherit autotools-brokensep pkgconfig lib_package xenclient

