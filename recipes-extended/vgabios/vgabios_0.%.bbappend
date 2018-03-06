FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += " \
    file://xen-fix-vbe-size-computation-overflow.patch \
    file://xen-fix-vbe-unsupported-mode.patch \
    file://xen-reduce-stack-usage.patch \
    file://xen-vbe-check-supported-vesa-mode.patch \
    file://xen-fix-vbe-win8.patch \
    file://xen-fix-print-format.patch \
    file://vbe-edid-interface.patch \
    file://vbe-extensions.patch \
    file://vga-spinlock.patch \
    file://vga-shadow-bda.patch \
    file://xen-log-to-ioport-0xe9.patch \
    file://vbe-extended-edid-modes.patch                        \
    file://vbe-xenvesa.patch                                    \
"

do_install_append() {
    install -m 0644 VGABIOS-lgpl-latest.debug.bin ${D}/usr/share/firmware/${PN}-${PV}.debug.bin
    install -m 0644 VGABIOS-lgpl-latest.cirrus.debug.bin ${D}/usr/share/firmware/${PN}-${PV}.cirrus.debug.bin
}
