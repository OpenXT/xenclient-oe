DESCRIPTION = "XenClient DBUS library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "dbus dbus-glib xenclient-idl xenclient-rpcgen-native libtool libevent"

SRC_URI = "${OPENXT_GIT_MIRROR}/libxcdbus.git;protocol=git;tag=${OPENXT_TAG}"

EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR}"

LICENSE="Proprietary"

S = "${WORKDIR}/git"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient

