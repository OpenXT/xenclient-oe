DESCRIPTION = "RPC Broker"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
DEPENDS = "json-c libwebsockets libv4v dbus libxml2"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/tijko/xctools.git;protocol=https;branch=master"

S = "${WORKDIR}/git/rpc-broker"

EXTRA_OECONF += ""
EXTRA_OEMAKE += ""

inherit autotools
inherit pkgconfig

