DESCRIPTION = "Application to fill pciback quirks"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641e94ec96f98fabc56ff9cc48be14b"
DEPENDS = "json-c pciutils"

PV = "0+git${SRCPV}"

SRC_URI = "git://github.com/achartier/heimdallr.git;protocol=git \
           file://pci-quirks.json \
           file://fix-json-pkgconfig-name.patch \
           "
SRCREV = "16b0da1e69e92ef8c0834e8a377c13aea823cfa2"

S = "${WORKDIR}/git"

# Hack to get CFLAGS not wiped out by OE
EXTRA_OEMAKE = ""

CFLAGS_append += "-Wno-deprecated-declarations"

inherit pkgconfig

do_install() {
        oe_runmake DESTDIR="${D}/usr/bin" install

        install -d ${D}${sysconfdir}
        install -m 0644 ${WORKDIR}/pci-quirks.json ${D}${sysconfdir}
}
