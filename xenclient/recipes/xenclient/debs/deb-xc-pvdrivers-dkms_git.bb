inherit xenclient
inherit xenclient-deb
inherit xenclient-deb-dkms

LICENSE = "GPLv2"
DEPENDS = "${@deb_bootstrap_deps(d)}"

PV = "0+git${SRCPV}"

SRCREV = "c1e92e8b6c6a74e326d96c807ab71c8f52e91179"
SRC_URI = "git://github.com/openxt/pv-linux-drivers.git;protocol=https"

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
