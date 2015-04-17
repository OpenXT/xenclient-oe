inherit xenclient
inherit xenclient-deb
inherit xenclient-deb-dkms

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
DEPENDS = "${@deb_bootstrap_deps(d)}"

PV = "0+git${SRCPV}"

SRCREV = "${AUTOREV}"
SRC_URI = "git://${OPENXT_GIT_MIRROR}/pv-linux-drivers.git;protocol=${OPENXT_GIT_PROTOCOL};branch=${OPENXT_BRANCH}"

S="${WORKDIR}/git"

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"

DEB_EXTRA_PKGS = "lintian"

DEB_NAME = "xc-pvdrivers-dkms"
DEB_DESC = "XenClient PV dkms drivers"


do_install() {
        mkdir -p "${D}/usr/share/${DEB_NAME}-${PR}"
	cp -a xc-* include/ Makefile Kbuild dkms.conf "${D}/usr/share/${DEB_NAME}-${PR}"
	mkdir -p "${D}/oe-for-staging"
        install -D -m 0644 "${S}/include/xen/v4v.h" "${D}/oe-for-staging/usr/include/xen/v4v.h"
        install -D -m 0644 "${S}/include/linux/v4v_dev.h" "${D}/oe-for-staging/usr/include/linux/v4v_dev.h"
}
