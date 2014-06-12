DESCRIPTION = "Application to fill pciback quirks"
# Aurelien has not specified a license for this project so we
# set it to CLOSED to keep bb happy.
LICENSE = "CLOSED"
DEPENDS = "libjson pciutils"

SRC_URI = "git://github.com/achartier/heimdallr.git;protocol=git \
           file://pci-quirks.json \
           "
SRCREV = "be536cc740314d4b035a549188cdd2310fd7e4bf"

S = "${WORKDIR}/git"

# Hack to get CFLAGS not wiped out by OE
EXTRA_OEMAKE = ""

do_install() {
        oe_runmake DESTDIR="${D}/usr/bin" install

        install -d ${D}${sysconfdir}
        install -m 0644 ${WORKDIR}/pci-quirks.json ${D}${sysconfdir}
}
