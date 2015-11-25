LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=8f2057d797dcf340e16719314cfd69b2"
DEPENDS = "openssl"
PR = "r2"

SRC_URI = "http://www.mmonit.com/monit/dist/monit-${PV}.tar.gz\
	file://init \
	file://monitrc \
	file://display_reboot"

INITSCRIPT_NAME = "monit"
INITSCRIPT_PARAMS = "start 99 5 . stop 00 0 1 6 ."

inherit autotools-brokensep update-rc.d

EXTRA_OECONF = "--without-ssl --without-pam libmonit_cv_setjmp_available=yes libmonit_cv_vsnprintf_c99_conformant=yes"

do_install_append() {
	install -d ${D}${sysconfdir}/init.d/
	install -m 755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/monit
	install -m 600 ${WORKDIR}/monitrc ${D}${sysconfdir}/monitrc
	install -m 755 ${WORKDIR}/display_reboot ${D}/usr/bin/display_reboot
}

CONFFILES_${PN} += "${sysconfdir}/monitrc"


SRC_URI[md5sum] = "9b01279316c6492973b224eb95fac687"
SRC_URI[sha256sum] = "8276b060b3f0e6453c9748d421dec044ddae09d3e4c4666e13472aab294d7c53"
