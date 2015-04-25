inherit xenclient
inherit xenclient-deb
inherit xenclient-deb-dkms

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
DEPENDS = "${@deb_bootstrap_deps(d)}"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/xctools.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

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

