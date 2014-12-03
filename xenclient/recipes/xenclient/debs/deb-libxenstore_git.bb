inherit xenclient
inherit xenclient-deb

require recipes/xen/xen.inc

LICENSE = "GPLv2"
DEPENDS = "${@deb_bootstrap_deps(d)}"

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"


DEB_NAME = "libxenstore3.0"
DEB_DESC="xenstore library"
DEB_DESC_EXT="This package provides the xenstore library."
DEB_SECTION="libs"
DEB_CREATEDEV="1"
DEB_EXTRA_PKGS = ""
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"

do_configure() {
	mkdir -p "oe-build/tools"
	touch config/Tools.mk
	( cd "tools/include" && make )
	cp -Lr "tools/include" "oe-build/tools"
	cp -Lr "tools/xenstore" "oe-build/tools"
	cp -Lr "config" "oe-build"
	cp -Lr "tools/Rules.mk" "oe-build/tools"
	cp -Lr "Config.mk" "oe-build"
}

do_compile() {
	cd oe-build && make -C tools/xenstore libxenstore.so
}

do_install() {
	pushd "${S}/oe-build/"
		mkdir -p "${D}/usr/lib"
		mkdir -p "${D}/usr/include"
		( cd tools/xenstore && rsync -a libxenstore.so* "${D}/usr/lib/" )
		( cd tools/xenstore && rsync -a xenstore.h xenstore_lib.h "${D}/usr/include/" )
		( cd "${S}/tools/include/" && DESTDIR="${D}" make install )
	popd

}
