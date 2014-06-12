inherit xenclient
inherit xenclient-pq
inherit xenclient-deb

LICENSE = "GPLv2"
DEPENDS = "${@deb_bootstrap_deps(d)} deb-libxenstore"

SRC_URI = "${XEN_SRC_URI} \
	   ${OPENXT_GIT_MIRROR}/xen-common-pq.git;protocol=git;tag=${OPENXT_TAG} \
"

SRC_URI[md5sum] := "${XEN_SRC_MD5SUM}"
SRC_URI[sha256sum] := "${XEN_SRC_SHA256SUM}"

S = "${WORKDIR}/xen-${XEN_VERSION}"

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
