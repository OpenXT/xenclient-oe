DESCRIPTION = "XenClient DBUS library"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=321bf41f280cf805086dd5a720b37785"
DEPENDS = "dbus dbus-glib xenclient-idl xenclient-rpcgen-native libtool libevent"

PV = "0+git${SRCPV}"

SRCREV = "754c65c18870e077f6b6b60006b396d43b37d65d"
SRC_URI = "git://github.com/openxt/libxcdbus.git;protocol=https"

EXTRA_OECONF += "--with-idldir=${STAGING_IDLDIR}"

LICENSE="Proprietary"

S = "${WORKDIR}/git"

inherit autotools
inherit pkgconfig
inherit lib_package
inherit xenclient

