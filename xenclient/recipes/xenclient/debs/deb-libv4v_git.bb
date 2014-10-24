
DESCRIPTION = "libv4v xctools"
LICENSE = "LGPLv2.1"

inherit xenclient
inherit xenclient-deb

DEPENDS = "${@deb_bootstrap_deps(d)}"
DEPENDS += " deb-xc-pvdrivers-dkms deb-libxenstore libtool"

export STAGING_KERNEL_DIR

PV = "git${SRCPV}"

SRCREV = "03df72706f45e568b0862672bb4768dd6c4c15b9"
SRC_URI = "git://github.com/openxt/v4v.git;protocol=https"

S="${WORKDIR}/git/libv4v"

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_NAME = "libv4v-1.0-0"
DEB_DESC="v4v library"
DEB_DESC_EXT=" This package provides the v4v library."
DEB_SECTION="libs"
DEB_CREATEDEV="1"
DEB_CREATE_SRC="1"
DEB_EXTRA_PKGS = ""
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

do_install() {
	DESTDIR="${D}" make install
	( set +e; cd "${S}/man" && DESTDIR="${D}" make install; exit 0 )
}
