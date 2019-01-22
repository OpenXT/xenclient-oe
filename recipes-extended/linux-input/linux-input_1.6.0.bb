SRC_URI = "https://svwh.dl.sourceforge.net/project/linuxconsole/linuxconsoletools-${PV}.tar.bz2 \
	file://snes232.patch \
	file://99-wacom-serial.rules \
"

SRC_URI[md5sum] = "fd52fa4a81455eb95a6c81efb087ce98"
SRC_URI[sha256sum] = "ced2efed00b67b45f82eddc69be07385835d558f658016315ac621fe2eaa8146"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://inputattach.c;beginline=18;endline=33;md5=cddf4f732a5c3cf90f973c746407781c"

S = "${WORKDIR}/linuxconsoletools-${PV}/utils"

do_compile() {
        oe_runmake inputattach
}

do_install() {
	install -d ${D}${sbindir}
	install inputattach ${D}${sbindir}
	install -d ${D}/etc/udev/rules.d
	install -c -m 655 ${WORKDIR}/99-wacom-serial.rules ${D}/etc/udev/rules.d/ 
}
