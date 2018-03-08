inherit xenclient
inherit xenclient-deb

require recipes-extended/xen/xen.inc

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
DEPENDS = "${@deb_bootstrap_deps(d)} deb-libxenstore"

RDEPENDS_${PN}-base_remove = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', '${PN}-blktap ${PN}-libblktapctl ${PN}-libvhd', d)} \
    "

RRECOMMENDS_${PN}-base_remove = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', '${PN}-libblktap', d)} \
    "

PACKAGES_remove = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'blktap2', '', '${PN}-blktap ${PN}-libblktap ${PN}-libblktapctl ${PN}-libblktapctl-dev ${PN}-libblktap-dev', d)} \
    "

DEB_SUITE = "wheezy"
DEB_ARCH = "i386"
DEB_CREATE_SRC="1"


DEB_NAME = "xenstore-client-tools"
DEB_DESC="Xenstore client tools"
DEB_SECTION="misc"
DEB_DESC_EXT="This package provides the xenstore client tools."
DEB_EXTRA_PKGS = ""
DEB_PKG_MAINTAINER = "Citrix Systems <customerservice@citrix.com>"
DEB_MAN_PAGE_NO_MMHELP = "1"

do_configure() {
	mkdir -p "oe-build/tools"
	touch config/Tools.mk
	( cd "tools/include" && make )
	cp -Lr "tools/include" "oe-build/tools"
	cp -Lr "tools/xenstore" "oe-build/tools"
	cp -Lr "config" "oe-build"
	cp -Lr "tools/Rules.mk" "oe-build/tools"
	cp -Lr "Config.mk" "oe-build"
	( mkdir -p "${D}/oe-for-src" && cd oe-build && cp -a . "${D}/oe-for-src/" )
}

do_compile() {
	cd oe-build && make -C tools/xenstore clients
}

do_install() {
	pushd "${S}/oe-build/"
		mkdir -p "${D}/usr/bin"
		( cd tools/xenstore && rsync -a xenstore xenstore-chmod xenstore-control xenstore-exists xenstore-list xenstore-ls xenstore-read xenstore-rm xenstore-watch xenstore-write "${D}/usr/bin/" )
	popd
}
