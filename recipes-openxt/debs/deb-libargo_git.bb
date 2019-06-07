
DESCRIPTION = "libargo xctools"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780"

inherit xenclient
inherit xenclient-deb

DEPENDS = "${@deb_bootstrap_deps(d)}"
DEPENDS += " deb-xc-pvdrivers-dkms deb-libxenstore libtool"

export STAGING_KERNEL_DIR

PV = "git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/linux-xen-argo.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S="${WORKDIR}/git/libargo"

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_NAME = "libargo-1.0-0"
DEB_DESC="argo library"
DEB_DESC_EXT=" This package provides the argo library."
DEB_SECTION="libs"
DEB_CREATEDEV="1"
DEB_CREATE_SRC="1"
DEB_EXTRA_PKGS = ""
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

do_install() {
	DESTDIR="${D}" make install
	( set +e; cd "${S}/man" && DESTDIR="${D}" make install; exit 0 )
}
