DESCRIPTION = "xblanker xctools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit xenclient
inherit xenclient-deb

DEPENDS = "${@deb_bootstrap_deps(d)}"
DEPENDS += " deb-libxenstore"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xblanker.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S="${WORKDIR}/git"

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"
DEB_CREATE_SRC="1"

DEB_NAME = "xblanker"
DEB_DESC="The xblanker package"
DEB_DESC_EXT="This package provides the xblanker utility."
DEB_SECTION="misc"
DEB_EXTRA_PKGS = "automake xorg-dev"
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

do_install() {
	DESTDIR="${D}" make install
	( set +e; cd "${S}/man" && DESTDIR="${D}" make install; exit 0 )
}

