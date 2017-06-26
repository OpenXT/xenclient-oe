FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://0004-tweak-MODPROBE_CMD-for-cross-compile.patch \
    file://yocto-initscripts.patch \
"

CACHED_CONFIGUREVARS += "MODPROBE_CMD=${base_sbindir}/modprobe"

# meta-oe recipe will already _append the autotools do_install(), and
# do_<something>_append() cannot be overridden...
# So instead, overwrite the files since this is a bbappend it should be done
# after the initial do_install_append()
do_install_append() {
    if ! ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        # Use Yocto compatible initscripts instead of the RHEL ones provided by
        # the tarball.
        oe_runmake 'DESTDIR=${D}' install install_initscripts_yocto
        mv -f ${D}${sysconfdir}/rc.d/init.d/* ${D}${sysconfdir}/init.d/
        rm -rf ${D}${sysconfdir}/rc.d
    fi
}

pkg_postinst_${PN}_append () {
#! /bin/sh
install -d /config/etc/lvm

RESTORECON="/sbin/restorecon"
[ -x "${RESTORECON}" ] && ${RESTORECON} -R -F /config/etc/lvm
}
