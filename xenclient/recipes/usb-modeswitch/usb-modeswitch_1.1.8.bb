DESCRIPTION = "A mode switching tool for controlling 'flip flop' (multiple device) USB gear"
SECTION = "base"
PRIORITY = "required"
LICENSE = "GPLv2"
DEPENDS = "virtual/libusb0"

FISH = "20110714"

SRC_URI = "${OPENXT_MIRROR}/usb-modeswitch-${PV}-jimsh.tar.bz2 \
           ${OPENXT_MIRROR}/usb-modeswitch-data-${FISH}.tar.bz2;name=modeswitch_data"
SRC_URI[md5sum] = "85d4ddea2ff28661d383bf30a63a96ad"
SRC_URI[sha256sum] = "4f705f24fe0a1e4443501f34e3a6654a1db4330f0b4a62debc8fb487ec3883a4"
SRC_URI[modeswitch_data.md5sum] = "da8ecaac36d97b5474d43d52fe66c272"
SRC_URI[modeswitch_data.sha256sum] = "f78891e77f38c7279f620013e357e59e0d43724d155cfb4d40c587c524cf19bf"


S = "${WORKDIR}/usb-modeswitch-${PV}-jimsh"
Q = "${WORKDIR}/usb-modeswitch-data-${FISH}"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f  \
                    file://../usb-modeswitch-data-${FISH}/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

PACKAGES = "${PN}"
FILES_${PN} = "/etc /var /usr/sbin /usr/bin /usr/share/usb_modeswitch /lib/udev"

do_compile() {
	oe_runmake
}

do_install() {
	install -d ${D}/usr/sbin
	install -d ${D}/etc/udev/rules.d
	install -d ${D}/usr/share/man/man1

	${MAKE} DESTDIR=${D} install

	cd ${Q}

	${MAKE} DESTDIR=${D} RULESDIR=${D}/etc/udev/rules.d files-install db-install

	rm -f ${D}/usr/bin/tclsh
	ln -sf jimsh ${D}/usr/bin/tclsh

}
