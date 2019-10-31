DESCRIPTION = "USB HID device dumping utility"

inherit autotools pkgconfig lib_package

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

DEPENDS += " libusb1 "

SRC_URI = "https://github.com/DIGImend/${PN}/releases/download/${PV}/${PN}-${PV}.tar.gz;name=tarball"
SRC_URI[tarball.md5sum] = "35a9d0d7febc98a0072a92fdf04d7194"
SRC_URI[tarball.sha256sum] = "065bdf713ca2446e455f3b71e6fc9d401dc694d73ac9f3c7b66940771660f46c"

FILES_${PN} += " /usr/bin/usbhid-dump "

S = "${WORKDIR}/${PN}-${PV}/"
