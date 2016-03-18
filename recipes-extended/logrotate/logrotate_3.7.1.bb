SRC_URI[md5sum] = "552639142e163745f6bcd4f1f3816d8a"
SRC_URI[sha256sum] = "7e79b03d65105541a5fdcc05087bee29ebc9e33149ac33d6563d0b20eddf79e0"
DESCRIPTION = "Rotates, compresses, removes and mails system log files"
HOMEPAGE = "http://packages.debian.org/unstable/admin/logrotate"
RCONFLICTS_${PN} = "logrotate-script"
DEPENDS = "popt"
RDEPENDS_${PN} = ""
SECTION = "admin"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760"
PR = "r3"

SRC_URI = "http://ftp.debian.org/debian/pool/main/l/logrotate/logrotate_${PV}.orig.tar.gz \
	   file://logrotate-3.7.1.patch;patch=1 \
           file://scriptexec.patch;patch=1 \
           file://logrotate.conf \
	   file://logrotate-wrapper"


CFLAGS_prepend  += "-I${STAGING_INCDIR} "
LDFLAGS_prepend += "-L${STAGING_LIBDIR} "
EXTRA_OEMAKE    =  "-p"

do_compile() {
    oe_runmake
}

do_install () {
    # Install the binary and conf file
    install -D -m 0755 ${S}/logrotate ${D}${base_sbindir}/logrotate
    install -D -m 0755 ${WORKDIR}/logrotate-wrapper ${D}${base_sbindir}/logrotate-wrapper
    install -D -m 0644 ${WORKDIR}/logrotate.conf ${D}${sysconfdir}/logrotate.conf
    install -m 0755 -d ${D}${sysconfdir}/logrotate.d
}

pkg_postinst_${PN} () {
    # Add the logrotate line to /etc/crontab
    grep -q "${base_sbindir}/logrotate" $D${sysconfdir}/crontab || echo "*/5 * * * *   root ${base_sbindir}/logrotate ${sysconfdir}/logrotate.conf" >> $D${sysconfdir}/crontab
}

pkg_postrm_${PN} () {
    # Remove the logrotate line from /etc/crontab
    grep -v ${base_sbindir}/logrotate ${sysconfdir}/crontab > ${sysconfdir}/crontab.no-${PF}
    mv ${sysconfdir}/crontab.no-${PF} ${sysconfdir}/crontab
}

CONFFILES_${PN} += "${sysconfdir}/logrotate.conf"
