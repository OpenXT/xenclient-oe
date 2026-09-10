DESCRIPTION = "Application to fill pciback quirks"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "json-c pciutils"

PV = "0+git${SRCPV}"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:${THISDIR}/patches:"
SRC_URI = "git://github.com/achartier/heimdallr.git;protocol=https;branch=master \
           file://pci-quirks.json \
           file://fix-json-pkgconfig-name.patch \
           file://json-0.13-interface-change.patch \
           "
SRCREV = "16b0da1e69e92ef8c0834e8a377c13aea823cfa2"


# Hack to get CFLAGS not wiped out by OE
EXTRA_OEMAKE = ""

CFLAGS:append = " -Wno-deprecated-declarations"

inherit pkgconfig

do_install() {
        oe_runmake DESTDIR="${D}/usr/bin" install

        install -d ${D}${sysconfdir}
        install -m 0644 ${UNPACKDIR}/pci-quirks.json ${D}${sysconfdir}
}
