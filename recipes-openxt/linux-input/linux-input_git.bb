SRCREV = "e4331cbc61dfe706c72b19febe65f368f4af0208"
PR = "r5"
PV = "0.0+git${SRCPV}"

SRC_URI = "git://git.code.sf.net/p/linuxconsole/code;protocol=https \
	file://serio.h \
	file://makefile.patch;patch=1;pnum=0 \
	file://snes232.patch;patch=1;pnum=0 \
	file://dont-set-cc.patch;patch=1;pnum=0 \
	file://wacom-support.patch;patch=1;pnum=0 \
        file://inputattach-resume-after-suspend.patch;patch=1 \
	file://99-wacom-serial.rules \
"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://inputattach.c;beginline=21;endline=39;md5=f38f0b28798dbf9b0cc59de32ef71a28"

S = "${WORKDIR}/git/ruby/utils"

CFLAGS =+ "-I. -I../linux/include -I../ruby-2.6/include"

do_configure() {
	install -d linux
	install -m 0644 ${WORKDIR}/serio.h linux/
}

do_install() {
	install -d ${D}${sbindir}
	install evtest inputattach ${D}${sbindir}
	install -d ${D}/etc/udev/rules.d
	install -c -m 655 ${WORKDIR}/99-wacom-serial.rules ${D}/etc/udev/rules.d/ 
}
