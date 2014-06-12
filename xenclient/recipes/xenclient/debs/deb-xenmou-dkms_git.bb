inherit xenclient
inherit xenclient-deb
inherit xenclient-deb-dkms

LICENSE = "GPLv2"
DEPENDS = "${@deb_bootstrap_deps(d)}"

SRC_URI = "${OPENXT_GIT_MIRROR}/xctools.git;protocol=git;tag=${OPENXT_TAG}"

S="${WORKDIR}/git/xenmou"

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_EXTRA_PKGS = "lintian"

DEB_NAME = "xenmou-dkms"
DEB_DESC = "xenmou linux driver"


do_install() {
        mkdir -p "${D}/usr/share/${DEB_NAME}-${PR}"
        cp -a Makefile xenmou.c "${D}/usr/share/${DEB_NAME}-${PR}"
}

