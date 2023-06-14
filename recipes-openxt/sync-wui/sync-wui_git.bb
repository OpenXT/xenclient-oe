DESCRIPTION = "XenClient sync wui"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "dojosdk-native"

XENCLIENT_BUILD ?= "unknown"
XENCLIENT_BUILD_DATE ?= "unknown"
XENCLIENT_BUILD_BRANCH ?= "unknown"
XENCLIENT_VERSION ?= "unknown"
XENCLIENT_RELEASE ?= "unknown"

PV = "0+git${SRCPV}"

SRCREV = "e37a0993f7821365e7dae1e969a837125df418de"
SRC_URI = "git://github.com/OpenXT/sync-wui.git;protocol=https"

S = "${WORKDIR}/git"

inherit package_tar

do_configure() {
	:
}

do_compile() {
    make

    cat <<EOF > VERSION
build = ${XENCLIENT_BUILD}
build_date = ${XENCLIENT_BUILD_DATE}
build_branch = ${XENCLIENT_BUILD_BRANCH}
version = ${XENCLIENT_VERSION}
release = ${XENCLIENT_RELEASE}
EOF
}

do_install() {
    make DESTDIR=${D} install

    cp VERSION ${D}/sync-wui/
    cp VERSION ${D}/sync-wui-sources/
}

do_populate_sysroot() {
	:
}

do_package_write_ipk() {
	:
}

PACKAGES += "${PN}-sources"

FILES_${PN} = "/sync-wui"
FILES_${PN}-sources = "/sync-wui-sources"
