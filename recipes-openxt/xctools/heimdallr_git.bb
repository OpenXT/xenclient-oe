DESCRIPTION = "Application to fill pciback quirks"
# Aurelien has not specified a license for this project so we
# set it to CLOSED to keep bb happy.
LICENSE = "CLOSED"
DEPENDS = "json-c pciutils"

SRC_URI = "git://github.com/achartier/heimdallr.git;protocol=git \
           file://pci-quirks.json \
           file://fix-json-pkgconfig-name.patch \
           "
SRCREV = "be536cc740314d4b035a549188cdd2310fd7e4bf"

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
