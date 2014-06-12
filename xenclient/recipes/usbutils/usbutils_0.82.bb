SRC_URI[md5sum] = "6e393cc7423b5d228fa3d34c21481ae4"
SRC_URI[sha256sum] = "9876b0e45a1bd3899222b916ab1d423e9efa3ad9374d55a6a301d5716f2d8a2f"
DESCRIPTION = "Host side USB console utilities."
SECTION = "base"
DEPENDS += "libusb-compat"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
PRIORITY = "optional"

SRC_URI = "${SOURCEFORGE_MIRROR}/linux-usb/usbutils-${PV}.tar.gz \
	  file://add_new_entries_to_usb_ids.patch;patch=1"
inherit autotools

EXTRA_OECONF = "--program-prefix="
sbindir = "/sbin"
bindir = "/bin"

FILES_${PN} += "${datadir}/usb*"

do_configure_prepend() {
	rm -rf ${S}/libusb
}
