SRC_URI[md5sum] = "ab675f5856a35f6aa0cd6ab057443ead"
SRC_URI[sha256sum] = "4aa1036e9ec468aa7ab38095969d363941c815dd1ba6fabcd593d16baef1b859"
DESCRIPTION = "Rsyslog logging daemon"
DEPENDS = "zlib"
LICENSE = "GPLv3&LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=51d9635e646fb75e1b74c074f788e973  \
                    file://COPYING.LESSER;md5=cb7903f1e5c39ae838209e130dca270a"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r3"

SRC_URI = "http://rsyslog.com/files/download/rsyslog/rsyslog-${PV}.tar.gz \
           file://rsyslog.conf                                           \
           file://rsyslog.init                                            \
           file://rsyslog.logrotate"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools update-rc.d pkgconfig

do_install_append() {
        install -d ${D}/${sysconfdir}/${PN}
        install ${WORKDIR}/rsyslog.conf ${D}${sysconfdir}/rsyslog.conf
        install -d ${D}/${sysconfdir}/init.d
        install -m 755 ${WORKDIR}/rsyslog.init ${D}/${sysconfdir}/init.d/rsyslog
        install -d ${D}/${sysconfdir}/logrotate.d
        install ${WORKDIR}/rsyslog.logrotate ${D}${sysconfdir}/logrotate.d/rsyslog
}

pkg_postinst_${PN}() {
        update-rc.d -f syslog remove
}

# TODO: Remove when syslogd is kicked out from our busybox.
pkg_postrm_${PN}() {
        update-rc.d syslog add 5
}

CONFFILES_${PN} = "${sysconfdir}/rsyslog.conf"
FILES_${PN} = "${bindir}/* ${sbindir}/* ${libexecdir}/* ${libdir}/lib*${SOLIBS} \
            ${sysconfdir} ${sharedstatedir} ${localstatedir} \
            ${base_bindir}/* ${base_sbindir}/* \
            ${base_libdir}/*${SOLIBS} \
            ${datadir}/${BPN} ${libdir}/${BPN}/*${SOLIBSDEV} \
            ${datadir}/include/scl/ ${datadir}/xsd"
FILES_${PN}-dev += "${libdir}/${BPN}/*.la"


INITSCRIPT_NAME = "rsyslog"
INITSCRIPT_PARAMS = "start 36 S ."
