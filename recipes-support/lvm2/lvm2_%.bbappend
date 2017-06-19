FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://0004-tweak-MODPROBE_CMD-for-cross-compile.patch \
    file://yocto-initscripts.patch \
"

CACHED_CONFIGUREVARS += "MODPROBE_CMD=${base_sbindir}/modprobe"

do_install() {
    autotools_do_install

    # Install machine specific configuration file
    install -m 0644 ${WORKDIR}/lvm.conf ${D}${sysconfdir}/lvm/lvm.conf
    sed -i -e 's:@libdir@:${libdir}:g' ${D}${sysconfdir}/lvm/lvm.conf
    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        oe_runmake 'DESTDIR=${D}' install install_systemd_units
        sed -i -e 's:/usr/bin/true:${base_bindir}/true:g' ${D}${systemd_system_unitdir}/blk-availability.service
    else
        # Use Yocto compatible initscripts instead of the RHEL ones provided by
        # the tarball.
        oe_runmake 'DESTDIR=${D}' install install_initscripts_yocto
    fi
}

pkg_postinst_${PN}_append () {
#! /bin/sh
install -d /config/etc/lvm

RESTORECON="/sbin/restorecon"
[ -x "${RESTORECON}" ] && ${RESTORECON} -R -F /config/etc/lvm
}
