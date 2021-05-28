FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += " \
    file://xen-fix-vbe-size-computation-overflow.patch \
    file://xen-fix-vbe-unsupported-mode.patch \
    file://xen-reduce-stack-usage.patch \
    file://xen-vbe-check-supported-vesa-mode.patch \
    file://xen-fix-vbe-win8.patch \
    file://xen-fix-print-format.patch \
    file://xen-log-to-ioport-0xe9.patch \
    file://vbe-extended-edid-modes.patch \
"

do_install_append() {
    install -m 0644 VGABIOS-lgpl-latest.debug.bin ${D}${datadir}/firmware/${BPN}-${PV}.debug.bin
    install -m 0644 VGABIOS-lgpl-latest.cirrus.debug.bin ${D}${datadir}/firmware/${BPN}-${PV}.cirrus.debug.bin
}
