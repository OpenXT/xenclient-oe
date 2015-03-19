require vgabios.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=dcf3c825659e82539645da41a7908589 \
                    "
DEPENDS = ""

SRC_URI += "file://xen-fix-vbe-size-computation-overflow.patch;patch=1  \
            file://xen-fix-vbe-unsupported-mode.patch;patch=1           \
            file://xen-reduce-stack-usage.patch;patch=1                 \
            file://xen-vbe-check-supported-vesa-mode.patch;patch=1      \
            file://xen-fix-vbe-win8.patch;patch=1                       \
            file://xen-fix-print-format.patch;patch=1                   \
            file://vbe-hvmloader-lfb-addr.patch;patch=1                 \
            file://vbe-edid-interface.patch;patch=1                     \
            file://vbe-extensions.patch;patch=1                         \
            file://vga-spinlock.patch;patch=1                           \
            file://vga-shadow-bda.patch;patch=1                         \
            "
SRC_URI[tarball.md5sum] = "2c0fe5c0ca08082a9293e3a7b23dc900"
SRC_URI[tarball.sha256sum] = "9d24c33d4bfb7831e2069cf3644936a53ef3de21d467872b54ce2ea30881b865"

PR = "r0"

FILES_${PN} = "/usr/share/firmware/${PN}-${PV}*.bin"
FILES_${PN}-dbg = "/usr/share/firmware/${PN}-${PV}*.debug.bin"

do_configure() {
    echo "Skip do_configure"
}

do_install() {
    install -d ${D}/usr/share/firmware
    install -m 0644 VGABIOS-lgpl-latest.bin ${D}/usr/share/firmware/${PN}-${PV}.bin
    install -m 0644 VGABIOS-lgpl-latest.cirrus.bin ${D}/usr/share/firmware/${PN}-${PV}.cirrus.bin
}

