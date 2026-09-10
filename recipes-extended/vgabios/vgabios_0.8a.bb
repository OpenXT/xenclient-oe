SUMMARY = "Plex86/Bochs LGPL VGA BIOS"
HOMEPAGE = "https://www.nongnu.org/vgabios/"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=fae731a3adbc92fd8bb1730d1f2455bc"

# meta-virtualization dropped this firmware recipe; Xen HVM still stages
# vgabios-0.8a.bin from the sysroot. Carry it here with OpenXT VBE patches.
SRC_URI = "${SAVANNAH_GNU_MIRROR}/vgabios/${BP}.tgz \
    file://xen-fix-vbe-size-computation-overflow.patch \
    file://xen-fix-vbe-unsupported-mode.patch \
    file://xen-reduce-stack-usage.patch \
    file://xen-vbe-check-supported-vesa-mode.patch \
    file://xen-fix-vbe-win8.patch \
    file://xen-fix-print-format.patch \
    file://xen-log-to-ioport-0xe9.patch \
    file://vbe-extended-edid-modes.patch \
"
SRC_URI[sha256sum] = "481042240ef0f1c918780c92a6bb42ad4d3f5d989b29502fa7ee7faf13a041b9"

S = "${UNPACKDIR}/${BP}"

COMPATIBLE_HOST = "(i.86|x86_64).*-linux"

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${datadir}/firmware
    install -m 0644 VGABIOS-lgpl-latest.bin ${D}${datadir}/firmware/${BPN}-${PV}.bin
    install -m 0644 VGABIOS-lgpl-latest.cirrus.bin ${D}${datadir}/firmware/${BPN}-${PV}.cirrus.bin
    install -m 0644 VGABIOS-lgpl-latest.debug.bin ${D}${datadir}/firmware/${BPN}-${PV}.debug.bin
    install -m 0644 VGABIOS-lgpl-latest.cirrus.debug.bin ${D}${datadir}/firmware/${BPN}-${PV}.cirrus.debug.bin
}

FILES:${PN} = "${datadir}/firmware"
INSANE_SKIP:${PN} += "arch"
